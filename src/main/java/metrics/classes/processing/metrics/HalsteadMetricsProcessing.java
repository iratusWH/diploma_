package metrics.classes.processing.metrics;

import com.github.javaparser.JavaToken;
import com.github.javaparser.Position;
import com.github.javaparser.Range;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static support.classes.OperatorsConstClass.*;

/**
 * StaticAnalyzer
 *
 * Класс вычисляющий метрики Холстеда
 * импортированные файлы не идут в расчет, т.к. многие IDE сами способны подписывать их в соответствии с программой
 *
 * @author Маркелов Александр A-07-18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class HalsteadMetricsProcessing extends MetricProcessingImpl {

    // шаблон форматированного вывода метрик
    private static final String RESULT_TEMPLATE =
            "total: operators - %s, operands - %s; \n"
            + "distinct: operators - %s, operands - %s; \n"
            + "vocabulary - %s, length - %s, volume - %.2f; \n"
            + "difficult - %.2f, effort - %.2f, programming time - %.2f m;";

    private List<String> allLexemes; // список всех лексем
    private List<String> allOperatorsList; // список всех операторов
    private List<String> allOperandsList; // список всех операндов

    private long distinctOperators; // уникальные операторы
    private long distinctOperands; // уникальные операнды
    private long totalOperators; // все операторы
    private long totalOperands; // все операнды

    private long vocabulary; // словарь программы
    private long programLength; // длина программы
    private double volume; // объем программы
    private double difficulty; // сложность программирования
    private double effort; // трудозатраты при программировании
    private double programmingTime; // время затрачиваемое на программирование

    private long totalQuotes; // дополнительная не выводимая метрика количества комментариев

    public HalsteadMetricsProcessing() {
        setMetricName(MetricNameEnum.HALSTEAD_METRICS);
    }

    @Override
    public void processMetric() {
            setAllLexemes(
                    getFile()
                            .findFirst(ClassOrInterfaceDeclaration.class)
                            .orElseThrow(),
                    getFile()
                            .getRange()
                            .orElseThrow()
            );// получение всех лексем

            processAllHalsteadMetrics(); // вычисление метрик
            setMetric(getFormattedResult()); // вывод метрик
    }

    // функция поиска всех лексем в программе
    private void setAllLexemes(ClassOrInterfaceDeclaration classDeclaration, Range classRange) {
        allLexemes = new ArrayList<>();

        // инициализация первого токена
        TokenRange tokenRange = classDeclaration.getTokenRange().orElseThrow();
        JavaToken currentToken = tokenRange.getBegin();
        Position endOfFile = classRange.end;
        Position endOfToken;

        do {
            allLexemes.add(Optional.of(currentToken.getText())
                    .filter(lex -> !isClosedBrackets(lex)) // фильтр закрытых скобок, операндом считается последовательность (), но считаем одну открытую скобку
                    .filter(lex -> !SPACE_STRING.equalsIgnoreCase(lex)) // фильтрация символов пробелов
                    .filter(lex -> !lex.startsWith(ONE_LINE_COMMENT)) // фильтрация однострочных комментариев
                    .filter(lex -> !lex.startsWith(JAVADOC_OR_MULTILINE_COMMENT)) // фильтрация многострочных комментариев
                    .orElse(EMPTY_STRING));

            endOfToken = getEndOfTokenPosition(currentToken);
            currentToken = currentToken.getNextToken().orElse(null);
        } while (endOfFile.isAfter(endOfToken) && Objects.nonNull(currentToken));

        allLexemes.removeIf(EMPTY_STRING::equals); // удаляем все пустые строки
        allLexemes.removeAll(ESCAPE_SEQUENCES); // удаляем все Escape-последовательности

        allLexemes.forEach(lex -> {
            if (lex.startsWith("\"")) totalQuotes++; // подсчет кол-ва
        });
    }

    // функция запуска подсчета метрик Холстеда
    private void processAllHalsteadMetrics() {
        saveTotalOperators();
        saveTotalOperands();
        saveDistinctOperators();
        saveDistinctOperands();
        processProgramVocabulary();
        processProgramLength();
        processProgramVolume();
        processProgramDifficulty();
        processProgramEffort();
        processProgrammingTime();
    }

    // подсчет всех операторов
    private void saveTotalOperators() {
        allOperatorsList = allLexemes.stream().filter(ALL_OPERATORS_LIST::contains).toList();
        setTotalOperators(allOperatorsList.size() + totalQuotes);
    }

    // подсчет всех операндов
    private void saveTotalOperands() {
        allOperandsList = new ArrayList<>(allLexemes);
        allOperandsList.removeAll(allOperatorsList);
        setTotalOperands(allOperandsList.size());
    }

    // подсчет уникальных операторов
    private void saveDistinctOperators() {
        setDistinctOperators(getAllOperatorsList().stream()
                .distinct()
                .count() + (getTotalOperators() >= 1 ? 1 : 0));
    }

    // подсчет уникальных операндов
    private void saveDistinctOperands() {
        setDistinctOperands(getAllOperandsList().stream()
                .distinct()
                .count()
        );
    }

    // подсчет словаря программа в соответствии с формулой:
    // Словарь программы = уникальные операнды + уникальные операторы
    private void processProgramVocabulary() {
        setVocabulary(getDistinctOperands() + getDistinctOperators());
    }

    // подсчет длины программы в соответствии с формулой:
    // Длина программы = операнды + операторы
    private void processProgramLength() {
        setProgramLength(getTotalOperands() + getTotalOperators());
    }

    // подсчет объема программы в соответствии с формулой:
    // Объем программы = длина программы * log2(словарь программы)
    private void processProgramVolume() {
        setVolume(getProgramLength() * Math.log(getVocabulary()) / Math.log(2));
    }

    // подсчет сложности программы в соответствии с формулой:
    // Сложность программы = (уникальные операторы/2) * (операнды/уникальные операнды)
    private void processProgramDifficulty() {
        setDifficulty((getDistinctOperators() / 2.0) * (getTotalOperands() * 1.0 / getDistinctOperands()));
    }

    // подсчет усилий при программировании в соответствии с формулой:
    // Усилия при программировании = Сложность программы * Объем программы
    private void processProgramEffort() {
        setEffort(getDifficulty() * getVolume());
    }

    // подсчет времени затрачиваемое для программирования в соответствии с формулой:
    // Время затрачиваемое для программирования = Усилия при программировании / 18 * коэффициент перевода в минуты
    private void processProgrammingTime(){
        setProgrammingTime(getEffort() / (60.0 * 18.0));
    }

    // проверка на закрытые скобки
    private boolean isClosedBrackets(String lexeme) {
        return CLOSE_FIGURE_BRACKET.equals(lexeme) || CLOSE_ROUND_BRACKET.equals(lexeme) || "]".equals(lexeme);
    }

    // получение позиции лексемы
    private Position getEndOfTokenPosition(JavaToken tokenRange) {
        return tokenRange.getRange()
                .orElseThrow()
                .end;
    }

    // получение форматированного вывода результатов метрик Холстеда
    private String getFormattedResult(){
        return String.format(RESULT_TEMPLATE,
                getTotalOperators(),
                getTotalOperands(),
                getDistinctOperators(),
                getDistinctOperands(),
                getVocabulary(),
                getProgramLength(),
                getVolume(),
                getDifficulty(),
                getEffort(),
                getProgrammingTime()
        );
    }

}

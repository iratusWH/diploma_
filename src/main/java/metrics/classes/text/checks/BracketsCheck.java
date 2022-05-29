package metrics.classes.text.checks;

import com.github.javaparser.JavaToken;
import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.*;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import org.apache.commons.lang3.StringUtils;
import support.classes.MetricNameEnum;

import java.util.*;

import static support.classes.OperatorsConstClass.*;

/**
 * StaticAnalyzer
 *
 * Класс проверки наличия фигурных скобок около операторов,
 * которые подразумевают их наличие, однако не запрещают отсутствия скобок
 *
 * @author Маркелов Александр A-07-18
 */
@Slf4j
public class BracketsCheck extends MetricProcessingImpl {

    // константа сообщения, которое сигнализирует о корректности класса
    private static final String OK_MESSAGE = "OK";

    public BracketsCheck() {
        setMetricName(MetricNameEnum.BRACKET_CHECK);
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
            // получение всех выражений подразумевающих наличие фигурных скобок
            List<Statement> allStatements = getAllStatementsWithFigureBrackets(compilationUnit);
            String missingFigureBracketString = OK_MESSAGE;

            // проверяем на наличие выражений подразумевающих наличие фигурных скобок, ищем около этих выражений отсутствующие скобки
            // записывая места обнаружения в список, затем преобразуя его к строке
            if (!allStatements.isEmpty()) {
                List<Position> missingFigureBracketsList = allStatements.stream().map(this::findMissingFigureBracket).toList();
                if (!missingFigureBracketsList.isEmpty()) {
                    missingFigureBracketString = formatPositionToString(missingFigureBracketsList);
                }
            }

            // заполняем поле метрики получившимся результатом, используя вспомогательную функцию форматированного вывода
            setMetric(
                    "".equals(missingFigureBracketString.trim()) ? OK_MESSAGE : missingFigureBracketString
            );
        } catch (Exception e) {
            log.error("BracketsCheck error - {}", e.getMessage());
        }
    }

    // получение всех выражений подразумевающих наличие фигурных скобок
    private List<Statement> getAllStatementsWithFigureBrackets(CompilationUnit compilationUnit) {
        List<Statement> statementsList = new ArrayList<>();

        statementsList.addAll(compilationUnit.findAll(IfStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(ForStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(ForEachStmt.class).stream().toList());
        statementsList.addAll(compilationUnit.findAll(WhileStmt.class).stream().toList());

        return statementsList;
    }

    // проверка на соответствие изначальной позиции лексемы
    private boolean isNotEqualHomePosition(Position otherPosition) {
        return !Position.HOME.equals(otherPosition);
    }

    // поиск отсутствующих фигурных скобок
    private Position findMissingFigureBracket(Statement startStatement) {

        // получение следующей лексемы после выражения
        Optional<JavaToken> nextToken = startStatement.getTokenRange()
                .map(TokenRange::getBegin);
        String currentToken;

        // поиск открывающей круглой скобки - окончания выражения
        do {
            nextToken = nextToken.map(JavaToken::getNextToken)
                    .filter(Optional::isPresent)
                    .map(Optional::get);

            currentToken = nextToken
                    .map(JavaToken::getText)
                    .orElseThrow();

        } while (!currentToken.equals(OPEN_ROUND_BRACKET));

        // поиск окончания выражения
        // с помощью дека происходит поиск окончания выражения
        Deque<Integer> bracketsStack = new ArrayDeque<>();
        do {
            // проверяем на соответствие текущей лексемы со списком открытых скобок
            if (OPEN_BRACKETS_LIST.contains(currentToken)) {
                bracketsStack.push(OPEN_BRACKETS_LIST.indexOf(currentToken));
            }

            // проверяем на соответствие текущей лексемы со списком закрытых скобок
            if (CLOSE_BRACKETS_LIST.contains(currentToken)
                    && (Objects.nonNull(bracketsStack.peek()))
                    && (bracketsStack.peek() == CLOSE_BRACKETS_LIST.indexOf(currentToken))) {
                bracketsStack.pop();
            }

            nextToken = nextToken.map(JavaToken::getNextToken)
                    .filter(Optional::isPresent)
                    .map(Optional::get);

            currentToken = nextToken
                    .map(JavaToken::getText)
                    .orElseThrow();

        } while (!bracketsStack.isEmpty()); // окончание произойдет если дэк опустеет,
        // т.е. все выражения окажутся завершенными

        // получаем позицию лексемы в коде
        Position statementPosition = getPosition(nextToken);

        // максимальное число итераций
        int statementCount = 7;
        do {
            // переход к не пустой лексеме
            nextToken = getNextNotEmptyToken(nextToken);

            currentToken = nextToken
                    .map(JavaToken::getText)
                    .orElseThrow();

            --statementCount;
        } while (!currentToken.equals(OPEN_FIGURE_BRACKET) && statementCount > 0); // выходим из цикла если находи открытую скобку

        // если текущая лексема равна открытой скобке, то выводим нулевую позицию в коде, если нет выводим последнюю сохраненную позицию
        return currentToken.equals(OPEN_FIGURE_BRACKET) ? Position.HOME : statementPosition;
    }

    // получение позиции лексемы
    private Position getPosition(Optional<JavaToken> token) {
        return token.map(JavaToken::getRange)
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .map(range -> range.begin)
                .orElseThrow();
    }

    // функция-фильтр пустых токенов
    private Optional<JavaToken> getNextNotEmptyToken(Optional<JavaToken> token) {
        String tokenText;
        do {
            token = token.map(JavaToken::getNextToken)
                    .filter(Optional::isPresent)
                    .map(Optional::get);

            tokenText = token.map(JavaToken::getText).orElseThrow();
        } while (tokenText.equals(StringUtils.EMPTY));
        return token;
    }

    // функция форматированного вывода
    private String formatPositionToString(List<Position> missingFigureBracketsList) {
        return String.join("\n",
                missingFigureBracketsList.stream()
                        .filter(this::isNotEqualHomePosition)
                        .map(Position::toString)
                        .filter(pos -> !StringUtils.EMPTY.equals(pos))
                        .toList()
        );
    }
}

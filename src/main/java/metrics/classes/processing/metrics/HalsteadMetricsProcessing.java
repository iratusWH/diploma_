package metrics.classes.processing.metrics;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static support.classes.OperatorsConstClass.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class HalsteadMetricsProcessing extends MetricProcessingImpl {

    private static final String RESULT_TEMPLATE =
            "total: operators - %s, operands - %s; \n"
            + "distinct: operators - %s, operands - %s; \n"
            + "vocabulary - %s, length - %s, volume - %.2f; \n"
            + "difficult - %.2f, effort - %.2f, programming time - %.2f s;";

    private List<String> allLexemes;
    private List<String> allOperatorsList;
    private List<String> allOperandsList;

    // Total metrics
    private long distinctOperators;
    private long distinctOperands;
    private long totalOperators;
    private long totalOperands;

    // Metrics for only class
    private long vocabulary;
    private long programLength;
    private double calcProgramLength;
    private double volume;
    private double difficulty;
    private double effort;
    private double programmingTime;

    // Support metric
    private long totalQuotes;

    public HalsteadMetricsProcessing() {
        setMetricName(MetricNameEnum.HALSTEAD_METRICS);
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
            setAllLexemes(compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow(), compilationUnit.getRange().orElseThrow());

            processAllHalsteadMetrics();
            setMetric(getFormattedResult());

        } catch (FileNotFoundException e) {
            log.error("HalsteadMetricsProcessing Error - {}", e.getMessage());
        }
    }

    private void setAllLexemes(ClassOrInterfaceDeclaration classDeclaration, Range classRange) {
        allLexemes = new ArrayList<>();

        TokenRange tokenRange = classDeclaration.getTokenRange().orElseThrow();

        JavaToken currentToken = tokenRange.getBegin();
        Position endOfFile = classRange.end;
        Position endOfToken;

        do {
            allLexemes.add(Optional.of(currentToken.getText())
                    .filter(lex -> !"\n".equalsIgnoreCase(lex))
                    .filter(lex -> !isClosedBrackets(lex))
                    .filter(lex -> !SPACE_STRING.equalsIgnoreCase(lex))
                    .filter(lex -> !lex.startsWith(ONE_LINE_COMMENT))
                    .filter(lex -> !lex.startsWith(JAVADOC_OR_MULTILINE_COMMENT))
                    .orElse(EMPTY_STRING));

            endOfToken = getEndOfTokenPosition(currentToken);
            currentToken = currentToken.getNextToken().orElse(null);
        } while (endOfFile.isAfter(endOfToken) && Objects.nonNull(currentToken));

        allLexemes.removeIf(EMPTY_STRING::equalsIgnoreCase);
        allLexemes.removeAll(ESCAPE_SEQUENCES);

        allLexemes.forEach(lex -> {
            if (lex.startsWith("\"")) totalQuotes++;
        });
    }

    private void processAllHalsteadMetrics() {
        getAndSaveTotalOperators();
        getAndSaveTotalOperands();
        getAndSaveDistinctOperators();
        getAndSaveDistinctOperands();
        processProgramVocabulary();
        processProgramLength();
        processProgramVolume();
        processProgramDifficulty();
        processProgramEffort();
        processProgrammingTime();
    }

    private void getAndSaveTotalOperators() {
        allOperatorsList = new ArrayList<>();
        allLexemes.forEach(this::setOperators);
        totalOperators = allOperatorsList.size() + totalQuotes;
    }

    private void getAndSaveTotalOperands() {
        allOperandsList = new ArrayList<>(allLexemes);
        allOperandsList.removeAll(allOperatorsList);
        totalOperands = allOperandsList.size();
    }

    private void getAndSaveDistinctOperators() {
        distinctOperators = getAllOperatorsList().stream().distinct().count() + (getTotalOperators() >= 1 ? 1 : 0);
    }

    private void getAndSaveDistinctOperands() {
        distinctOperands = getAllOperandsList().stream().distinct().count();
    }

    private void processProgramVocabulary() {
        setVocabulary(getDistinctOperands() + getDistinctOperators());
    }

    private void processProgramLength() {
        setProgramLength(getTotalOperands() + getTotalOperators());
    }

    private void processProgramVolume() {
        setVolume(getProgramLength() * Math.log(getVocabulary()) / Math.log(2));
    }

    private void processProgramDifficulty() {
        setDifficulty((getDistinctOperators() / 2.0) * (getTotalOperands() * 1.0 / getDistinctOperands()));
    }

    private void processProgramEffort() {
        setEffort(getDifficulty() * getVolume());
    }

    private void processProgrammingTime(){
        setProgrammingTime(getEffort() / (60.0 * 18.0));
    }

    private boolean isClosedBrackets(String lexeme) {
        return CLOSE_FIGURE_BRACKET.equals(lexeme) || CLOSE_ROUND_BRACKET.equals(lexeme) || "]".equals(lexeme);
    }

    void setOperators(String lex) {
        if (ALL_OPERATORS_LIST.contains(lex)) allOperatorsList.add(lex);
    }

    private Position getEndOfTokenPosition(JavaToken tokenRange) {
        return tokenRange.getRange()
                .orElseThrow()
                .end;
    }

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

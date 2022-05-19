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

    // Support metric
    private long totalQuotes;

    public HalsteadMetricsProcessing() {
        setMetricName(MetricNameEnum.HALSTEAD_METRICS);
        distinctOperands = 0;
        distinctOperators = 0;
        totalOperands = 0;
        totalOperators = 0;
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
            setAllLexemes(compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow(), compilationUnit.getRange().orElseThrow());

            processAllHalsteadMetrics();
            log.info("file name - {}: ", getFile().getName());
            log.info("total: operators - {}, operands - {}", getTotalOperators(), getTotalOperands());
            log.info("distinct: operators - {}, operands - {}", getDistinctOperators(), getDistinctOperands());
            log.info("vocabulary - {}, length - {}, volume - {}", getVocabulary(), getProgramLength(), getVolume());
            log.info("difficult - {}, effort - {}", getDifficulty(), getEffort());
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
    }

    private long getAndSaveTotalOperators() {
        allOperatorsList = new ArrayList<>();
        allLexemes.forEach(this::setOperators);
        totalOperators = allOperatorsList.size() + totalQuotes;

        return totalOperators;
    }

    private long getAndSaveTotalOperands() {
        allOperandsList = new ArrayList<>(allLexemes);
        allOperandsList.removeAll(allOperatorsList);
        totalOperands = allOperandsList.size();

        return totalOperands;
    }

    private long getAndSaveDistinctOperators() {
        distinctOperators = getAllOperatorsList().stream().distinct().count() + (getTotalOperators() >= 1 ? 1 : 0);
        return distinctOperators;
    }

    private long getAndSaveDistinctOperands() {
        distinctOperands = getAllOperandsList().stream().distinct().count();
        return distinctOperands;
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

    private boolean isClosedBrackets(String lexeme) {
        return "}".equals(lexeme) || ")".equals(lexeme) || "]".equals(lexeme);
    }

    void setOperators(String lex) {
        if (ALL_OPERATORS_LIST.contains(lex)) allOperatorsList.add(lex);
    }

    private Position getEndOfTokenPosition(JavaToken tokenRange) {
        return tokenRange.getRange()
                .orElseThrow()
                .end;
    }

}

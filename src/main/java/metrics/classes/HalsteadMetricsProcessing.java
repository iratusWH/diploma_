package metrics.classes;

import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.io.FileNotFoundException;
import java.util.*;

import static support.classes.OperatorsConstClass.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Slf4j
public class HalsteadMetricsProcessing extends MetricProcessingImpl {

    private List<String> allLexemes;
    private List<String> allOperatorsList;

    // Total metrics
    private int distinctOperators;
    private int distinctOperands;
    private int totalOperators;
    private int totalOperands;

    // Metrics for only class
    private int vocabulary;
    private int programLength;
    private double calcProgramLength;
    private double volume;
    private double difficulty;
    private double effort;
    private double timeReqProgram;
    private double timeDelBugs;

    public HalsteadMetricsProcessing() {
        setMetricName(MetricNameEnum.HALSTEAD_METRICS);
        distinctOperands = 0;
        distinctOperators = 0;
        totalOperands = 0;
        totalOperators = 0;
    }

    private void setAllLexemes(ClassOrInterfaceDeclaration classDeclaration, Range classRange) {
        TokenRange tokenRange = classDeclaration.getTokenRange().orElseThrow();

        JavaToken currentToken = tokenRange.getBegin();
        Position endOfFile = classRange.end;
        Position endOfToken;

        allLexemes = new ArrayList<>();
        do {
            allLexemes.add(Optional.of(currentToken.getText()).filter(lex -> !"\n".equalsIgnoreCase(lex)).orElse(""));
            endOfToken = getEndOfTokenPosition(currentToken);
            currentToken = currentToken.getNextToken().orElse(null);
        } while (endOfFile.isAfter(endOfToken) && Objects.nonNull(currentToken));

        allLexemes.removeIf(lex -> SPACE_STRING.equalsIgnoreCase(lex) || EMPTY_STRING.equalsIgnoreCase(lex));
    }

    private int getAndSaveTotalOperators() {
        allOperatorsList = new ArrayList<>();
        allLexemes.forEach(this::setOperators);
        return 0;
    }

    private int getAndSaveTotalOperands() {


        return 0;
    }

    void setOperators(String lex){

            if (OPERATORS_SEPARATORS_LIST.contains(lex)) allOperatorsList.add(lex);
            if (OPERATORS_KEYWORDS_LIST.contains(lex)) allOperatorsList.add(lex);
            if (OPERATORS_LIST.contains(lex)) allOperatorsList.add(lex);

    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
            setAllLexemes(compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow(), compilationUnit.getRange().orElseThrow());

            log.info("{}, {}, {}", getAndSaveTotalOperators(), getFile().getName(), allLexemes);
        } catch (FileNotFoundException e) {
            log.error("HalsteadMetricsProcessing Error - {}", e.getMessage());
        }
    }

    private Position getEndOfTokenPosition(JavaToken tokenRange) {
        return tokenRange.getRange()
                .orElseThrow()
                .end;
    }


}

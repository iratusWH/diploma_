package metrics.classes.text.checks;

import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
public class VariableOnNewLineCheck extends SimpleMetricProcessingImpl {

    Predicate<NodeWithVariables<?>> isNotOnlyVar = declaration -> declaration.getVariables().size() != 1;

    public VariableOnNewLineCheck() {
        setMetric(MetricNameEnum.VARIABLE_DECLARATION_ON_NEW_LINE_CHECK);
    }

    @Override
    public void processMetric() {
        List<String> fieldPositionList = getPositionOfMultiplyDeclaration(FieldDeclaration.class);
        List<String> variablePositionList = getPositionOfMultiplyDeclaration(VariableDeclarationExpr.class);

        setMetric(variablePositionList.isEmpty() && fieldPositionList.isEmpty() ? "OK" : formattedString(fieldPositionList, variablePositionList));
    }

    private <T extends Node & NodeWithVariables<?>> List<String> getPositionOfMultiplyDeclaration(Class<T> variableClass) {
        return getFile()
                .findAll(variableClass)
                .stream()
                .filter(isNotOnlyVar)
                .map(Node::getRange)
                .map(Optional::orElseThrow)
                .map(range -> range.begin)
                .map(Position::toString)
                .toList();
    }

    private String formattedString(List<String> fields, List<String> vars) {
        return listAsString(fields) + ", " + listAsString(vars);
    }

    private String listAsString(List<String> list) {
        return list.toString().replace("[", "").replace("]", "");
    }
}

package metrics.classes.text.checks;

import com.github.javaparser.Position;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithVariables;
import com.github.javaparser.ast.stmt.ForStmt;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
public class VariableOnNewLineCheck extends SimpleMetricProcessingImpl {

    Predicate<NodeWithVariables<?>> isNotOnlyVar = declaration -> declaration.getVariables().size() != 1;

    public VariableOnNewLineCheck() {
        setMetricName(MetricNameEnum.VARIABLE_DECLARATION_ON_NEW_LINE_CHECK);
    }

    @Override
    public void processMetric() {
        List<String> fieldPositionList = getPositionOfMultiplyDeclaration(FieldDeclaration.class);
        List<String> variablePositionList = getPositionOfMultiplyDeclaration(VariableDeclarationExpr.class);

        setMetric(formattedString(fieldPositionList, variablePositionList));
    }

    private <T extends Node & NodeWithVariables<?>> List<String> getPositionOfMultiplyDeclaration(Class<T> variableClass) {
        return getFile()
                .findAll(variableClass)
                .stream()
                .filter(isNotOnlyVar)
                .filter(this::isDeclarationsNotInForCycle)
                .map(Node::getRange)
                .map(Optional::orElseThrow)
                .map(range -> range.begin)
                .map(Position::toString)
                .toList();
    }

    private String formattedString(List<String> fields, List<String> vars) {

        if (vars.isEmpty() && fields.isEmpty()){
            return "OK";
        }

        if (fields.isEmpty()) {
            return listAsString(vars);
        }

        if (vars.isEmpty()) {
            return listAsString(fields);
        }

        return listAsString(fields)
                + ", "
                + listAsString(vars);

    }

    private String listAsString(List<String> list) {
        return list.toString()
                .replace("[", "")
                .replace("]", "");
    }

    private <T extends Node & NodeWithVariables<?>> boolean isDeclarationsNotInForCycle(T variableNode){
        return variableNode
                .getParentNode()
                .map(this::noneInstanceOfFor)
                .orElse(Boolean.TRUE);
    }

    private boolean noneInstanceOfFor(Node node){
        return !(node instanceof ForStmt);
    }

}

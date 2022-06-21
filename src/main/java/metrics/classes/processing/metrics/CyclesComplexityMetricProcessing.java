package metrics.classes.processing.metrics;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Slf4j
@EqualsAndHashCode(callSuper = true)
public class CyclesComplexityMetricProcessing extends SimpleMetricProcessingImpl {

    private static final Predicate<Node> isStatement = statement -> statement instanceof ForEachStmt
            || statement instanceof ForStmt
            || statement instanceof IfStmt
            || statement instanceof DoStmt
            || statement instanceof SwitchEntry
            || statement instanceof WhileStmt
            || statement instanceof TryStmt
            || statement instanceof CatchClause;


    public CyclesComplexityMetricProcessing() {
        setMetricName(MetricNameEnum.CYCLES_COMPLEXITY_METRIC);
    }
    private String methodName;
    private Integer tempDepth;
    @Override
    public void processMetric() {
        methodName = "";
        tempDepth = 0;
        var depth = getAllMethodStmt(getFile())
                .stream()
                .map(method -> {
                        var dep = getDepth(method, 0);
                        if (tempDepth < dep) {
                            tempDepth = dep;
                            methodName = method.getNameAsString();
                        }
                        return dep;
                })
                .max(Integer::compare)
                .orElse(0);

        setMetric( depth == 0 ? "Class hasn't branching statements" : "Max depth of nesting in method <underline>" + methodName + "</underline>: "+ depth);
    }

    List<MethodDeclaration> getAllMethodStmt(CompilationUnit file) {
        return Optional.ofNullable(file.findAll(MethodDeclaration.class)).orElse(new ArrayList<>());
    }

    List<Node> getChildNodesStatements(Node node) {
        if (node.getChildNodes()
                .stream()
                .anyMatch(BlockStmt.class::isInstance)) {

            return node.stream().filter(BlockStmt.class::isInstance).map(BlockStmt.class::cast)
                    .map(Node::getChildNodes)
                    .flatMap(List::stream)
                    .filter(isStatement)
                    .toList();
        }

        return node.getChildNodes()
                .stream()
                .filter(isStatement)
                .toList();
    }

    Integer getDepth(Node node, Integer depth) {

        List<Node> nodes = getChildNodesStatements(node);

        if (!nodes.isEmpty()) {
            return nodes
                    .stream()
                    .map(innerNode -> getDepth(innerNode, depth + 1))
                    .max(Integer::compare)
                    .orElse(depth + 1);
        }

        return depth;
    }

}

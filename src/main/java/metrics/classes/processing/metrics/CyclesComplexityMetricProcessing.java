package metrics.classes.processing.metrics;

import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
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
            || statement instanceof CatchClause;


    public CyclesComplexityMetricProcessing() {
        setMetricName(MetricNameEnum.CYCLES_COMPLEXITY_METRIC);
    }

    @Override
    public void processMetric() {
       var depth = getAllMethodStmt(getFile())
               .stream()
               .map(this::getChildNodesStatements)
               .flatMap(List::stream)
               .map(this::getDepth)
               .max(Integer::compare)
               .orElse(0);

       log.info("{}", depth);

       setMetric(depth);
    }

    List<MethodDeclaration> getAllMethodStmt(CompilationUnit file) {
        return file.findAll(MethodDeclaration.class);
    }

    List<Node> getChildNodesStatements(Node node) {
        return node.getChildNodes()
                .stream()
                .filter(isStatement)
                .toList();
    }

    Integer getDepth(Node node) {

        List<Node> nodes = getChildNodesStatements(node);

        if (!nodes.isEmpty()) {
            return nodes
                    .stream()
                    .map(this::getDepth)
                    .max(Integer::compare)
                    .orElse(0);
        }

        return 0;
    }

}

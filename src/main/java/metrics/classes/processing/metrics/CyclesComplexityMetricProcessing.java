package metrics.classes.processing.metrics;

import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import lombok.*;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@EqualsAndHashCode(callSuper = true)
public class CyclesComplexityMetricProcessing extends SimpleMetricProcessingImpl {

    private static final Predicate<Node> isStatement = statement -> statement instanceof ForEachStmt
            || statement instanceof ForStmt
            || statement instanceof IfStmt
            || statement instanceof DoStmt
            || statement instanceof SwitchEntry
            || statement instanceof WhileStmt
            || statement instanceof CatchClause;

    private final Function<Node, NodeWithPosition> composeNodeWithPosition = node -> NodeWithPosition
            .builder()
            .node(node)
            .position(getNodePosition(node))
            .build();

    CyclesComplexityMetricProcessing() {
        setMetricName(MetricNameEnum.CYCLES_COMPLEXITY_METRIC);
    }

    @Override
    public void processMetric() {


    }

    private Integer depthOfMethodBranching(MethodWithNodes method) {
        return method.getBranchingList()
                .stream()
                .map(node -> visitNode(node, 1))
                .flatMap(List::stream)
                .map(NodeWithPosition::getDepth)
                .max(Integer::compare)
                .orElse(0);
    }


    private List<NodeWithPosition> visitNode(NodeWithPosition statement, Integer depth) {
        statement.setDepth(depth);

        if (statement.getNode()
                .getChildNodes()
                .stream()
                .noneMatch(isStatement)) {
            return Collections.emptyList();
        }

        return statement.getNode()
                .getChildNodes()
                .stream()
                .filter(isStatement)
                .map(composeNodeWithPosition)
                .map(node -> visitNode(node, depth + 1))
                .flatMap(List::stream)
                .toList();
    }

    private List<MethodWithNodes> getAllNodesWithBody(CompilationUnit parsedFile) {
        return parsedFile
                .findAll(MethodDeclaration.class)
                .stream()
                .map(this::getMethodWithNodesByMethodDeclaration)
                .toList();
    }

    private MethodWithNodes getMethodWithNodesByMethodDeclaration(MethodDeclaration method) {
        return MethodWithNodes
                .builder()
                .branchingList(getAllBranchingStmt(method))
                .methodName(method.getNameAsString())
                .build();
    }

    private List<NodeWithPosition> getAllBranchingStmt(Node method) {
        return method.stream()
                .map(Node::getChildNodes)
                .flatMap(List::stream)
                .filter(isStatement)
                .map(composeNodeWithPosition)
                .toList();
    }

    private Position getNodePosition(Node node) {
        return node
                .getBegin()
                .orElse(Position.HOME);
    }

    @Builder
    @Getter
    private static class MethodWithNodes {
        private String methodName;
        private List<NodeWithPosition> branchingList;
        @Setter private int maxDepth;
    }

    @Builder
    @Getter
    private static class NodeWithPosition {
        private Node node;
        private Position position;
        @Setter private int depth;
    }
}

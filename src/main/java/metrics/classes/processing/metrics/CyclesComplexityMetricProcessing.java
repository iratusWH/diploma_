package metrics.classes.processing.metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.stmt.*;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import testCases.MethodDeclarationBuilder;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class CyclesComplexityMetricProcessing extends MetricProcessingImpl {

    private Map<String, Integer> methodsWithDepth;

    @Override
    public void processMetric() {
        try {
            methodsWithDepth = new HashMap<>();

            CompilationUnit cu = StaticJavaParser.parse(getFile());
            List<MethodDeclaration> methods = cu.findAll(MethodDeclaration.class);

            List<List<Node>> statements;
            methods.stream()
                    .map(this::getAllStatements)
                    .toList();

            log.info("complete");
        } catch (Exception e) {

        }
    }

    Integer processDepth(List<Node> statements){
        Integer maxDepth = 0;
        Integer tempDepth = 0;


    }

    List<Node> getAllStatements(MethodDeclaration method) {
        return method.stream()
                .filter(statement -> (statement instanceof ForEachStmt)
                        || (statement instanceof ForStmt)
                        || (statement instanceof IfStmt)
                        || (statement instanceof DoStmt)
                        || (statement instanceof SwitchEntry)
                        || (statement instanceof WhileStmt)
                        || (statement instanceof CatchClause))
                .toList();
    }

    boolean hasChildren(Node node){
        return !node.getChildNodes().isEmpty();
    }

    void deepness(Integer maxDepth, Integer tempDepth, List<Node> children){
        children.stream().filter(this::hasChildren);
    }

    Node getStatement(Node node){
        if (node instanceof Statement){
            return node;
        }
        return
    }
}

package metrics.classes.oop.antipattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.SimpleName;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.Arrays;
import java.util.List;

public class ObjectCesspoolAntipattern extends SimpleMetricProcessingImpl {

    private static final String OBJECT_POOL_PATTERN_KEYWORD = "ObjectPool";
    private static final String OBJECT_POOL_PATTERN_SHORT_KEYWORD = "Pool";

    private static final List<String> synonymsList = Arrays.asList(
            "release",
            "expire",
            "clear",
            "delete",
            "remove",
            "close",
            "clean",
            "destruct",
            "destroy"
    );

    public ObjectCesspoolAntipattern() {
        setMetricName(MetricNameEnum.OBJECT_CESSPOOL_ANTIPATTERN);
    }

    @Override
    public void processMetric() {
        List<ClassOrInterfaceDeclaration> classes = getFile().findAll(ClassOrInterfaceDeclaration.class)
                .stream()
                .filter(name -> name.getNameAsString().contains(OBJECT_POOL_PATTERN_KEYWORD)
                        || name.getNameAsString().contains(OBJECT_POOL_PATTERN_SHORT_KEYWORD)
                )
                .toList();

        setMetric(classes.isEmpty() ? "OK" : hasPoolClass(classes));
    }

    private String hasPoolClass(List<ClassOrInterfaceDeclaration> classes) {
        return hasDestroyMethod(classes) ? "Object Pool with object destroy method" : "Object cesspool";
    }

    private boolean hasDestroyMethod(List<ClassOrInterfaceDeclaration> classes) {
        return classes.stream()
                .map(Node::getChildNodes)
                .flatMap(List::stream)
                .filter(MethodDeclaration.class::isInstance)
                .map(Node::getChildNodes)
                .flatMap(List::stream)
                .filter(SimpleName.class::isInstance)
                .map(toMethodName -> ((SimpleName) toMethodName).asString())
                .anyMatch(methodName -> synonymsList.stream()
                        .anyMatch(methodName::startsWith));
    }
}
package metrics.classes.oop.antipattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import metrics.classes.implementations.SimpleMetricProcessingImpl;

import java.util.List;

public class ObjectCesspoolAntipattern extends SimpleMetricProcessingImpl {

    private static final String OBJECT_POOL_PATTERN_KEYWORD = "ObjectPool";

    @Override
    public void processMetric() {
//        List<ClassOrInterfaceDeclaration> classes = getFile().findAll(ClassOrInterfaceDeclaration.class);
//        classes.stream()
//                .filter(cl -> cl.getNameAsString().contains(OBJECT_POOL_PATTERN_KEYWORD))
//                .map(Node::getChildNodes)
//                .flatMap(List::stream)
//                .filter(FieldDeclaration.class::isInstance)
//                .map(Node::getChildNodes)
//                .flatMap(List::stream)
//                .filter()

        ;

    }
}

package metrics.classes.oop.antipattern;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import com.github.javaparser.metamodel.AnnotationExprMetaModel;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
public class AnemicDomainModelAntipattern extends SimpleMetricProcessingImpl {

    //    private final Consumer<AnnotationDeclaration> nameOfAnnotation = (name)
    private final Predicate<MarkerAnnotationExpr> dataAnnotation = annotation -> annotation.getNameAsString().contains("Data");

    @Override
    public void processMetric() {
        // Getter or Setter or data annotation
        // check the fields that not annotate but has a methods get/set

        findAnnotationBeforeClass();
    }

    private boolean findAnnotationBeforeClass() {

        log.info("{}", getFile().findAll(MarkerAnnotationExpr.class));
        hasOnlyFields();
        return false;
    }

    private void hasOnlyFields() {
        List<NodeList<VariableDeclarator>> fields = getFile().findAll(FieldDeclaration.class).stream().map(FieldDeclaration::getVariables).toList();



        log.info("{}, {}",
                fields,
                getFile().findAll(MethodDeclaration.class)
                        .stream()
                        .map(NodeWithSimpleName::getNameAsString)
                        .toList());
    }

    private String changeFirstChar(String field) {
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }
}
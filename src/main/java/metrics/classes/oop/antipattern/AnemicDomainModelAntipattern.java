package metrics.classes.oop.antipattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Slf4j
public class AnemicDomainModelAntipattern extends SimpleMetricProcessingImpl {

    private final Predicate<MarkerAnnotationExpr> dataAnnotation = annotation -> annotation.getNameAsString().contains("Data");
    private static final Predicate<String> noneServiceMethods = name -> !"equals".equals(name) || !"hashCode".equals(name) || !"toString".equals(name);

    NodeList<BodyDeclaration<?>> allClassNodes;
    List<BodyDeclaration<?>> fieldsDeclarations;
    List<String> fieldsNames;
    List<BodyDeclaration<?>> methodsDeclarations;
    List<String> methodsNames;
    Integer methodsCount;

    public AnemicDomainModelAntipattern(){
        setMetricName(MetricNameEnum.ANEMIC_DOMAIN_MODEL_ANTIPATTERN);
    }

    @Override
    public void processMetric() {
        methodsCount = 0;
        // Getter or Setter or data annotation
        // check the fields that not annotate but has a methods get/set
        getAllClassMembers();
        getClassFields();
        getAllClassMethods();

        setMetric(hasGetterSetterMethods());
        log.info("{}", hasDataAnnotationBeforeClass() && hasOnlyFields() || hasOnlyFields());

    }

    private void getAllClassMembers() {
        allClassNodes = getFile()
                .findFirst(ClassOrInterfaceDeclaration.class)
                .map(ClassOrInterfaceDeclaration::getMembers)
                .orElseGet(NodeList::new);
    }

    private void getClassFields() {
        fieldsDeclarations = allClassNodes
                .stream()
                .filter(FieldDeclaration.class::isInstance)
                .toList();

        fieldsNames = fieldsDeclarations
                .stream()
                .map(Node::getChildNodes)
                .map(nodes -> nodes
                        .stream()
                        .filter(VariableDeclarator.class::isInstance)
                        .findFirst()
                        .orElseThrow()
                )
                .map(node -> ((VariableDeclarator) node).getNameAsString())
                .toList();
    }

    private boolean hasDataAnnotationBeforeClass() {
        return !getFile()
                .findFirst(ClassOrInterfaceDeclaration.class)
                .map(Node::getChildNodes)
                .orElse(Collections.emptyList())
                .stream()
                .filter(MarkerAnnotationExpr.class::isInstance)
                .map(node -> ((MarkerAnnotationExpr) node).getNameAsString())
                .filter(name -> name.contains("Data")
                        || name.contains("Builder")
                        || name.contains("Getter")
                        || name.contains("Setter")
                )
                .toList()
                .isEmpty();
    }

    private boolean hasOnlyFields() {
        return !allClassNodes.isEmpty() && fieldsDeclarations.size() == allClassNodes.size();
    }

    private void getAllClassMethods() {
        methodsDeclarations = allClassNodes
                .stream()
                .filter(MethodDeclaration.class::isInstance)
                .toList();

        methodsNames = methodsDeclarations
                .stream()
                .map(method -> ((MethodDeclaration) method).getNameAsString())
                .filter(noneServiceMethods)
                .toList();

    }

    private boolean hasGetterSetterMethods() {
        return fieldsNames.size() == fieldsNames
                .stream()
                .filter(field -> !methodsNames
                        .stream()
                        .filter(method -> {
                            if (isSetOrGetMethod(method, field)) {
                                methodsCount += 1;
                                return true;
                            }

                            return false;
                        })
                        .toList()
                        .isEmpty()
                )
                .toList()
                .size()
                && methodsCount == fieldsNames.size();

    }

    private boolean isSetOrGetMethod(String methodName, String fieldName) {
        return methodName.contains(toSetMethodName(fieldName))
                || methodName.contains(toGetMethodName(fieldName));
    }

    private String toGetMethodName(String nameField) {
        return "get" + changeFirstChar(nameField);
    }

    private String toSetMethodName(String nameField) {
        return "set" + changeFirstChar(nameField);
    }

    private String changeFirstChar(String field) {
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }
}
package metrics.classes.oop.antipattern;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
public class AnemicDomainModelAntipattern extends SimpleMetricProcessingImpl {

    private final Predicate<MarkerAnnotationExpr> dataAnnotation = annotation -> annotation.getNameAsString().contains("Data");


    NodeList<BodyDeclaration<?>> allClassNodes;
    List<BodyDeclaration<?>> fieldsDeclarations;
    List<String> fieldsNames;
    List<BodyDeclaration<?>> methodsDeclarations;
    List<String> methodsNames;
    Long methodsCount;
    Long fieldsMethodsCount;

    public AnemicDomainModelAntipattern(){
        setMetricName(MetricNameEnum.ANEMIC_DOMAIN_MODEL_ANTIPATTERN);
    }

    @Override
    public void processMetric() {
        getAllClassMembers();

        getClassFields();
        getAllClassMethods();
        hasGetterSetterMethods();

        setMetric(hasOnlyFields() || hasGetterSetterMethods() || hasOnlyFields() && hasDataAnnotationBeforeClass() ? "Domain model class" : "a Usual class");
        log.info("{}", hasDataAnnotationBeforeClass() && hasOnlyFields() || hasOnlyFields());

    }

    private void getAllClassMembers() {
        allClassNodes = getFile()
                .findFirst(ClassOrInterfaceDeclaration.class)
                .map(TypeDeclaration::getMembers)
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
                        .toList()
                )
                .flatMap(List::stream)
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

        methodsCount = methodsDeclarations
                .stream()
                .map(method -> ((MethodDeclaration) method).getNameAsString())
                .count();

    }

    private boolean hasGetterSetterMethods() {

        methodsNames = methodsDeclarations.stream()
                .map(Node::getChildNodes)
                .filter(SimpleName.class::isInstance)
                .flatMap(List::stream)
                .map(node -> ((SimpleName) node).asString())
                .toList();

        fieldsMethodsCount = fieldsNames
                .stream()
                .filter(field -> !methodsNames
                        .stream()
                        .filter(method -> {
                            if (isSetOrGetMethod(method, field) || isServiceMethod(method)) {
                                methodsCount += 1;
                                return true;
                            }

                            return false;
                        })
                        .toList()
                        .isEmpty()
                )
                .count();

        return fieldsNames.size() > fieldsMethodsCount && Objects.equals(methodsCount, fieldsMethodsCount);

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

    private boolean isServiceMethod(String name) {
        return "equals".equals(name) || "hashCode".equals(name) || "toString".equals(name);
    }
}
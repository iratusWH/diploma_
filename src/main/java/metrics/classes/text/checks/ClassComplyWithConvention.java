package metrics.classes.text.checks;

import com.github.javaparser.Position;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;
import org.apache.commons.lang3.StringUtils;
import support.classes.MetricNameEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static support.classes.RegexPatterns.CLASS_OR_INTERFACE_DECLARATION_TEMPLATE;
import static support.classes.RegexPatterns.CONST_DECLARATION_TEMPLATE;
import static support.classes.RegexPatterns.VARIABLE_DECLARATION_TEMPLATE;

/**
 * Класс проверяющий исследуемый файл на соответствие конвенции о названии классов, переменных и констант в Java-программах
 * Допущение: все переменные написаны с новой строки
 * прим:
 * int a;
 * int b;
 */
@EqualsAndHashCode(callSuper = false)
@Slf4j
public class ClassComplyWithConvention extends MetricProcessingImpl {

    // шаблоны вывода различных ошибок
    private static final String OK_MESSAGE = "OK";
    private static final String CLASS_PROBLEM = "Classes or interfaces aren't comply with conventions: ";
    private static final String CONST_PROBLEM = "Constants aren't comply with conventions: ";
    private static final String VARIABLE_PROBLEM = "Variables aren't comply with conventions: ";
    private static final String FORMATTING_TEMPLATE = "Variable: %s; Position: %s";

    public ClassComplyWithConvention() {
        setMetricName(MetricNameEnum.CLASS_COMPLY_WITH_CONVENTION);
    }

    @Override
    public void processMetric() {
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
            List<String> listOfMismatch = new ArrayList<>();

            // вызов функций поиска ошибочных названий
            findAndPutProblemClasses(listOfMismatch, compilationUnit);
            findAndPutProblemConstants(listOfMismatch, compilationUnit);
            findAndPutProblemVariables(listOfMismatch, compilationUnit);

            setMetric(listOfMismatch.isEmpty() ? OK_MESSAGE : String.join(";\n", listOfMismatch));
        } catch (Exception e) {
            log.error("ClassComplyWithConvention error - {}", e.getMessage());
        }
    }

    // поиск всех возможных ошибок при написании классов
    private void findAndPutProblemClasses(List<String> problemsList, CompilationUnit compilationUnit) {
        Pattern patternForClassOrInterface = Pattern.compile(CLASS_OR_INTERFACE_DECLARATION_TEMPLATE);
        List<String> classOrInterfaceList = compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
                .stream()
                .map(ClassOrInterfaceDeclaration::getNameAsString)
                .filter(
                        declaration -> !patternForClassOrInterface.matcher(declaration)
                                .matches())
                .toList();

        if (!classOrInterfaceList.isEmpty()) {
            problemsList.add(CLASS_PROBLEM + String.join(", ", classOrInterfaceList));
        }
    }

    // поиск всех возможных ошибок при написании констант
    private void findAndPutProblemConstants(List<String> problemsList, CompilationUnit compilationUnit) {
        List<String> constantsList = compilationUnit.findAll(FieldDeclaration.class)
                .stream()
                .filter(FieldDeclaration::isFinal)
                .filter(FieldDeclaration::isStatic)
                .filter(declaration -> isNotVariableMatchPattern(declaration, CONST_DECLARATION_TEMPLATE))
                .map(this::formatVariableDeclaration)
                .toList();

        if (!constantsList.isEmpty()) {
            problemsList.add(CONST_PROBLEM + String.join(", ", constantsList));
        }
    }

    // поиск всех возможных ошибок при написании переменной
    private void findAndPutProblemVariables(List<String> problemsList, CompilationUnit compilationUnit) {
        List<String> variablesList = compilationUnit.findAll(FieldDeclaration.class)
                .stream()
                .filter(this::isNotStaticVariable)
                .filter(declaration -> isNotVariableMatchPattern(declaration, VARIABLE_DECLARATION_TEMPLATE))
                .map(this::formatVariableDeclaration)
                .toList();

        if (!variablesList.isEmpty()) {
            problemsList.add(VARIABLE_PROBLEM + String.join(", ", variablesList));
        }
    }

    // проверка на соответствие паттерну
    private boolean isNotVariableMatchPattern(FieldDeclaration declaration, String pattern) {
        Pattern patternForConst = Pattern.compile(pattern);

        return !patternForConst
                .matcher(
                        declaration
                                .getVariable(0)
                                .getNameAsString())
                .matches();
    }

    // функция выводящая форматированную строку с ошибочной переменной и ее позицией в коде
    private String formatVariableDeclaration(FieldDeclaration declaration) {
        String declarationName = declaration.getVariable(0).getNameAsString();
        String declarationPosition = declaration.getVariable(0).getBegin()
                .map(Position::toString)
                .orElse(StringUtils.EMPTY);

        return String.format(FORMATTING_TEMPLATE, declarationName, declarationPosition);
    }

    // проверка на свойство переменной быть static - константой
    private boolean isNotStaticVariable(FieldDeclaration declaration) {
        return !declaration.isStatic();
    }
}

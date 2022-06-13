package metrics.classes.oop.antipattern;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import lombok.EqualsAndHashCode;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.function.Predicate;

@EqualsAndHashCode(callSuper = false)
public class GodObjectAntipattern extends SimpleMetricProcessingImpl {

    private static final Long METHODS_COUNT = 5L;
    private static final Long FIELDS_COUNT = 5L;
    private static final Long CONSTANTS_COUNT = 5L;

    private static final String GOD_OBJECT_CONST = "Class is a God Object";
    private static final String DATA_CLASS_CONST = "Class is a Data class";
    private static final String CONSTANTS_CLASS_CONST = "Class is a Constants class";
    private static final String UTILS_CLASS_CONST = "Class is a Utils class";
    private static final String USUAL_CLASS_CONST = "a Usual class";

    private final Predicate<FieldDeclaration> isConstant = field -> field.isStatic() && field.isFinal();

    public GodObjectAntipattern() {
        setMetricName(MetricNameEnum.GOD_OBJECT_ANTIPATTERN);
    }

    @Override
    public void processMetric() {
        Integer methodsCount = getFile()
                .findAll(MethodDeclaration.class)
                .size();

        Long staticMethodsCount = getFile()
                .findAll(MethodDeclaration.class)
                .stream()
                .filter(MethodDeclaration::isStatic)
                .count();

        var fieldsCount = getFile()
                .findAll(ClassOrInterfaceDeclaration.class)
                .stream()
                .map(cl -> cl.getChildNodes())
                .toList();

        Long constantsCount = getFile()
                .findAll(FieldDeclaration.class)
                .stream()
                .filter(isConstant)
                .count();

//        setMetric(getResultByClassInfo(staticMethodsCount, methodsCount, constantsCount, fieldsCount));
    }

    private String getResultByClassInfo(Long staticMethodsCount, Integer methodsCount, Long constantsCount, Integer fieldsCount) {
        boolean isUtilClass = staticMethodsCount > METHODS_COUNT;
        boolean isConstantsClass = constantsCount > CONSTANTS_COUNT && staticMethodsCount == 0 && methodsCount == 0 && fieldsCount == 0;
        boolean isDataClass = fieldsCount > FIELDS_COUNT && staticMethodsCount == 0 && methodsCount < 4;

        if (fieldsCount > FIELDS_COUNT && constantsCount > CONSTANTS_COUNT && staticMethodsCount > METHODS_COUNT && methodsCount > METHODS_COUNT) {
            return GOD_OBJECT_CONST;
        }

        if (isDataClass) {
            return DATA_CLASS_CONST;
        }

        if (isConstantsClass) {
            return CONSTANTS_CLASS_CONST;
        }

        if (isUtilClass) {
            return UTILS_CLASS_CONST;
        }

        return USUAL_CLASS_CONST;
    }
}

package metrics.classes.oop.antipattern;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import lombok.EqualsAndHashCode;
import metrics.classes.implementations.SimpleMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.util.function.Predicate;

@EqualsAndHashCode(callSuper = false)
public class GodObjectAntipattern extends SimpleMetricProcessingImpl {

    private static final Long METHODS_COUNT = 10L;
    private static final Long FIELDS_COUNT = 10L;
    private static final Long CONSTANTS_COUNT = 10L;

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

        Integer fieldsCount = getFile()
                .findAll(FieldDeclaration.class)
                .size();

        Long constantsCount = getFile()
                .findAll(FieldDeclaration.class)
                .stream()
                .filter(isConstant)
                .count();

        setMetric(getResultByClassInfo(methodsCount, fieldsCount, constantsCount));
    }

    private String getResultByClassInfo(Integer methodsCount, Integer constantsCount, Long fieldsCount) {
        boolean isUtilClass = methodsCount > METHODS_COUNT;
        boolean isConstantsClass = constantsCount > CONSTANTS_COUNT;
        boolean isDataClass = fieldsCount > FIELDS_COUNT;

        if (isUtilClass && isConstantsClass && isDataClass) {
            return GOD_OBJECT_CONST;
        }

        if (isDataClass && !isConstantsClass && !isUtilClass) {
            return DATA_CLASS_CONST;
        }

        if (!isDataClass && isConstantsClass && !isUtilClass) {
            return CONSTANTS_CLASS_CONST;
        }

        if (!isDataClass && !isConstantsClass && isUtilClass) {
            return UTILS_CLASS_CONST;
        }

        return USUAL_CLASS_CONST;
    }
}

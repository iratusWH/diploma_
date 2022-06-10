package support.classes;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Перечисление констант-названий метрик
 */
@Getter
@RequiredArgsConstructor
public enum MetricNameEnum {
    COUPLING_BETWEEN_OBJECTS_METRIC("Coupling between Objects Metric"),
    CYCLES_COMPLEXITY_METRIC("Cycles Complexity Metric"),
    CYCLOMATIC_COMPLEXITY_METRIC("Cyclomatic Complexity Metric"),
    DEPTH_OF_INHERITANCE_METRIC("Depth of Inheritance Metric"),
    HALSTEAD_METRICS("Halstead Metrics"),
    MAINTAINABILITY_INDEX_METRIC("Maintainability Index Metric"),
    LINES_OF_CODE_METRIC("Lines of code"),
    BRACKET_CHECK("Bracket Check"),
    CLASS_COMPLY_WITH_CONVENTION_CHECK("Class comply with convention check"),
    GOD_OBJECT_ANTIPATTERN("God Object Antipattern"),
    ANEMIC_DOMAIN_MODEL_ANTIPATTERN("Anemic Domain Model Antipattern");


    final String name;
}

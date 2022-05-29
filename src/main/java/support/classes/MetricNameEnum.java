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
    CLASS_COMPLY_WITH_CONVENTION("Class comply with convention check"),
    CALL_SUPER_ANTI_PATTERN("Call super antipattern");

    final String name;
}

package metrics.interfaces;

import com.github.javaparser.ast.CompilationUnit;

/**
 * Интерфейс простой метрики, отвечающий за основные свойства метрик,
 * которые требуют для анализа один файл
 */
public interface SimpleMetricProcessing extends MetricProcessing {
    void setFile(CompilationUnit file);
}

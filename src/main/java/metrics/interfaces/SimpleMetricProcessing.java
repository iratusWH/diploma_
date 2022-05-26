package metrics.interfaces;

import java.io.File;


/**
 * Интерфейс простой метрики, отвечающий за основные свойства метрик,
 * которые требуют для анализа один файл
 */
public interface SimpleMetricProcessing extends MetricProcessing {
    void setFile(File file);
}

package metrics.interfaces;

/**
 * Интерфейс отвечающий за основные свойства всех классов метрик
 */
public interface MetricProcessing {
    Object getMetric();
    void processMetric();
}

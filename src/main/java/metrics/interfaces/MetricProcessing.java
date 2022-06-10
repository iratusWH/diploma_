package metrics.interfaces;

import support.classes.HTMLComponent;

/**
 * Интерфейс отвечающий за основные свойства всех классов метрик
 */
public interface MetricProcessing {
    Object getMetric();
    void processMetric();
    HTMLComponent getHtmlComponent();
}

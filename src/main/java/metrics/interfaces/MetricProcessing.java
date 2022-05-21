package metrics.interfaces;

import support.classes.HTMLComponent;

public interface MetricProcessing {
    Object getMetric();
    void processMetric();
    HTMLComponent getHTMLComponent();
}

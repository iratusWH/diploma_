package metrics.interfaces;

import support.classes.HTMLComponent;

public interface MetricProcessing {
    void processMetric();
    void printMetric();
    HTMLComponent getHTMLComponent();
}

package metrics.interfaces;

import support.classes.HTMLComponent;

import java.io.FileNotFoundException;

public interface MetricProcessing {
    void processMetric() throws FileNotFoundException;
    HTMLComponent getHTMLComponent();
}

package support.classes;

import lombok.Data;

@Data
public class HTMLComponent {
    private final MetricNameEnum metricName;
    private final String directoryAndFileName;
    private final String metric;
}

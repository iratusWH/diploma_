package support.classes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HTMLComponent {
    private final MetricNameEnum metricName;
    private final String directoryAndFileName;
    private final String metric;
}

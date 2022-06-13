package support.classes;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class HTMLComponent {
    private static final String DIV_METRIC_WRAPPER = "<div class='metric_wrapper'>";
    private static final String DIV_STARTING_METRIC_NAME = "<div class='metric_name'>";
    private static final String DIV_STARTING_CLASS_FILE_PATH = "<div class='file_path'>";
    private static final String DIV_STARTING_CLASS_METRIC_RESULT = "<div class='metric_result'>";
    private static final String DIV_ENDING = "</div>\n";

    private MetricNameEnum metricName;
    private String directoryAndFileName;
    private String metric;

    public void setMetricName(MetricNameEnum metricName) {
        this.metricName = Objects.nonNull(metricName) ? metricName : MetricNameEnum.DEFAULT_METRIC_NAME;
    }

    public String composeHTML(){
        return  DIV_METRIC_WRAPPER +
                    DIV_STARTING_METRIC_NAME +
                            this.getMetricName().getName() +
                    DIV_ENDING +
                    DIV_STARTING_CLASS_FILE_PATH +
                            this.getDirectoryAndFileName() +
                    DIV_ENDING +
                    DIV_STARTING_CLASS_METRIC_RESULT +
                            this.getMetric() +
                    DIV_ENDING +
                DIV_ENDING;
    }
}

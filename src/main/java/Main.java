import metrics.classes.CyclomaticComplexityMetricProcessing;
import metrics.classes.HalsteadMetricsProcessing;
import metrics.classes.ResourceFiles;

public class Main {
    public static void main(String[] args) {

        ResourceFiles resourceFiles = new ResourceFiles("/home/xela/IdeaProjects/diploma/src/main/java");

        CyclomaticComplexityMetricProcessing ccMetric = new CyclomaticComplexityMetricProcessing();
        HalsteadMetricsProcessing hMetric = new HalsteadMetricsProcessing();
        resourceFiles.getFileList()
                .forEach(
                        file -> {
                            ccMetric.setFile(file);
                            ccMetric.processMetric();
                            ccMetric.printMetric();
                            hMetric.setFile(file);
                            hMetric.processMetric();
                        }
                );
    }
}


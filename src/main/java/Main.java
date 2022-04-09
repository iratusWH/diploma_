import metrics.classes.CouplingBetweenObjectsMetricProcessing;
import metrics.classes.CyclomaticComplexityMetricProcessing;
import metrics.classes.DepthOfInheritanceTreeMetricProcessing;
import metrics.classes.HalsteadMetricsProcessing;
import support.classes.ResourceFiles;

public class Main {
    public static void main(String[] args) {

        ResourceFiles resourceFiles = new ResourceFiles("/home/xela/IdeaProjects/diploma/src/main/java");

        CyclomaticComplexityMetricProcessing ccMetric = new CyclomaticComplexityMetricProcessing();
        CouplingBetweenObjectsMetricProcessing cboMetric = new CouplingBetweenObjectsMetricProcessing();
        DepthOfInheritanceTreeMetricProcessing ditMetric = new DepthOfInheritanceTreeMetricProcessing();
        resourceFiles.getFileList()
                .forEach(
                        file -> {
                            ccMetric.setFile(file);
                            ccMetric.processMetric();
                            ccMetric.printMetric();
                            cboMetric.setFile(file);
                            cboMetric.processMetric();
                            cboMetric.printMetric();


                        }
                );

        ditMetric.setFileList(resourceFiles.getFileList());
        ditMetric.processMetric();
        ditMetric.printMetric();
    }
}


import lombok.extern.slf4j.Slf4j;
import metrics.classes.CouplingBetweenObjectsMetricProcessing;
//import metrics.classes.CyclomaticComplexityMetricProcessing;
import metrics.classes.DepthOfInheritanceTreeMetricProcessing;
import metrics.classes.HalsteadMetricsProcessing;
import support.classes.ResourceFiles;

@Slf4j
public class Main {
    public static void main(String[] args) {

        ResourceFiles resourceFiles = new ResourceFiles("/home/xela/IdeaProjects/diploma");

//        CyclomaticComplexityMetricProcessing ccMetric = new CyclomaticComplexityMetricProcessing();
//        CouplingBetweenObjectsMetricProcessing cboMetric = new CouplingBetweenObjectsMetricProcessing();
        HalsteadMetricsProcessing hmMetric = new HalsteadMetricsProcessing();
//        DepthOfInheritanceTreeMetricProcessing ditMetric = new DepthOfInheritanceTreeMetricProcessing();
        resourceFiles.filterFileList();

        resourceFiles.getFilteredFileList()
                .forEach(
                        file -> {
//                            ccMetric.setFile(file);
//                            ccMetric.processMetric();
//                            ccMetric.printMetric();

                            hmMetric.setFile(file);
                            hmMetric.processMetric();
//                            hmMetric.getHTMLComponent();


//                            cboMetric.setFile(file);
//                            cboMetric.processMetric();
//                            cboMetric.printMetric();
//                            log.info("obj {}", cboMetric.getHTMLComponent());
                        }
                );

//        ditMetric.setFileList(resourceFiles);
//        ditMetric.processMetric();
//        log.info("{}", ditMetric.getHTMLComponent());
    }
}


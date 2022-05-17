import lombok.extern.slf4j.Slf4j;
//import metrics.classes.processingMetrics.CyclomaticComplexityMetricProcessing;
import metrics.classes.processingMetrics.CyclomaticComplexityMetricProcessing;
import metrics.classes.processingMetrics.HalsteadMetricsProcessing;
import metrics.classes.processingMetrics.LOCMetricsProcessing;
import metrics.classes.processingMetrics.MaintainabilityIndexMetricProcessing;
import metrics.classes.text.checks.BracketsCheck;
import support.classes.ResourceFiles;

@Slf4j
public class Main {
    public static void main(String[] args) {

        ResourceFiles resourceFiles = new ResourceFiles("/home/xela/IdeaProjects/diploma");

//        CouplingBetweenObjectsMetricProcessing cboMetric = new CouplingBetweenObjectsMetricProcessing();
//        DepthOfInheritanceTreeMetricProcessing ditMetric = new DepthOfInheritanceTreeMetricProcessing();
//        CyclomaticComplexityMetricProcessing ccMetric = new CyclomaticComplexityMetricProcessing();
//        HalsteadMetricsProcessing hmMetric = new HalsteadMetricsProcessing();
//        LOCMetricsProcessing locMetric = new LOCMetricsProcessing();
        BracketsCheck bracketsCheck = new BracketsCheck();

        resourceFiles.filterFileList();

        resourceFiles.getFilteredFileList()
                .forEach(
                        file -> {
                            bracketsCheck.setFile(file);
                            bracketsCheck.processMetric();
                            log.info("BracketsCheck: {} ", bracketsCheck.getHTMLComponent());
//                            ccMetric.setFile(file);
//                            ccMetric.processMetric();
//
//                            hmMetric.setFile(file);
//                            hmMetric.processMetric();
//
//                            locMetric.setFile(file);
//                            locMetric.processMetric();
//
//                            MaintainabilityIndexMetricProcessing miMetric = new MaintainabilityIndexMetricProcessing(ccMetric, hmMetric, locMetric);
//                            miMetric.processMetric();
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


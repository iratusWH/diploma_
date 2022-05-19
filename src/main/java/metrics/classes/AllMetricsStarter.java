package metrics.classes;

import lombok.extern.slf4j.Slf4j;
import metrics.classes.processing.metrics.*;
import metrics.classes.text.checks.BracketsCheck;
import support.classes.ResourceFiles;

import java.io.File;

@Slf4j
public class AllMetricsStarter {
    ResourceFiles resourceFiles;

    private static final CouplingBetweenObjectsMetricProcessing cboMetric = new CouplingBetweenObjectsMetricProcessing();
    private static final DepthOfInheritanceTreeMetricProcessing ditMetric = new DepthOfInheritanceTreeMetricProcessing();
    private static final CyclomaticComplexityMetricProcessing ccMetric = new CyclomaticComplexityMetricProcessing();
    private static final HalsteadMetricsProcessing hmMetric = new HalsteadMetricsProcessing();
    private static final LOCMetricsProcessing locMetric = new LOCMetricsProcessing();
    private static final BracketsCheck bracketsCheck = new BracketsCheck();
    private static final MaintainabilityIndexMetricProcessing miMetric = new MaintainabilityIndexMetricProcessing();

    private AllMetricsStarter(String fullProjectPath){
        resourceFiles = new ResourceFiles(fullProjectPath);
    }

    public static AllMetricsStarter getStarter(String fullProjectPath){
        return new AllMetricsStarter(fullProjectPath);
    }

    public void execute(){
        resourceFiles.filterFileList();
        resourceFiles.getFilteredFileList().forEach(this::doSimpleMetrics);
        doComplexMetrics(resourceFiles);
    }

    private void doComplexMetrics(ResourceFiles resourceFiles){
        ditMetric.setFileList(resourceFiles);
        ditMetric.processMetric();
        log.info("DepthOfInheritanceTreeMetricProcessing: {}", ditMetric.getHTMLComponent());
    }

    private void doSimpleMetrics(File file){
        bracketsCheck.setFile(file);
        bracketsCheck.processMetric();
        log.info("BracketsCheck: {} ", bracketsCheck.getHTMLComponent());

        ccMetric.setFile(file);
        ccMetric.processMetric();
        log.info("CyclomaticComplexityMetricProcessing: {} ", ccMetric.getHTMLComponent());

        hmMetric.setFile(file);
        hmMetric.processMetric();
        log.info("HalsteadMetricsProcessing: {} ", ccMetric.getHTMLComponent());

        locMetric.setFile(file);
        locMetric.processMetric();
        log.info("LOCMetricsProcessing: {} ", ccMetric.getHTMLComponent());

        miMetric.setMetrics(ccMetric, hmMetric, locMetric);
        miMetric.processMetric();
        log.info("MaintainabilityIndexMetricProcessing: {}", miMetric.getHTMLComponent());

        cboMetric.setFile(file);
        cboMetric.processMetric();
        cboMetric.printMetric();
        log.info("CouplingBetweenObjectsMetricProcessing: {}", cboMetric.getHTMLComponent());
    }
}

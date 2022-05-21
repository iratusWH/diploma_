package metrics.classes;

import lombok.extern.slf4j.Slf4j;
import metrics.classes.processing.metrics.*;
import metrics.classes.text.checks.BracketsCheck;
import metrics.classes.text.checks.ClassComplyWithConvention;
import support.classes.ResourceFiles;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
public class AllMetricsStarter {
    ResourceFiles resourceFiles;

    private final CouplingBetweenObjectsMetricProcessing cboMetric;
    private final DepthOfInheritanceTreeMetricProcessing ditMetric;
    private final CyclomaticComplexityMetricProcessing ccMetric;
    private final HalsteadMetricsProcessing hmMetric;
    private final LOCMetricsProcessing locMetric;
    private final BracketsCheck bracketsCheck;
    private final MaintainabilityIndexMetricProcessing miMetric;
    private final ClassComplyWithConvention complyWithConventionCheck;

    private AllMetricsStarter(String fullProjectPath){
        resourceFiles = new ResourceFiles(fullProjectPath);
        cboMetric = new CouplingBetweenObjectsMetricProcessing();
        ditMetric = new DepthOfInheritanceTreeMetricProcessing();
        ccMetric = new CyclomaticComplexityMetricProcessing();
        hmMetric = new HalsteadMetricsProcessing();
        locMetric = new LOCMetricsProcessing();
        bracketsCheck = new BracketsCheck();
        miMetric = new MaintainabilityIndexMetricProcessing();
        complyWithConventionCheck = new ClassComplyWithConvention();
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

    private void doSimpleMetrics(File file) {
        log.info("File: {}", file.getPath());

//        bracketsCheck.setFile(file);
//        bracketsCheck.processMetric();
//        log.info("BracketsCheck: {} ", bracketsCheck.getHTMLComponent());
//
//        ccMetric.setFile(file);
//        ccMetric.processMetric();
//        log.info("CyclomaticComplexityMetricProcessing: {} ", ccMetric.getHTMLComponent());
//
//        hmMetric.setFile(file);
//        hmMetric.processMetric();
//        log.info("HalsteadMetricsProcessing: {} ", ccMetric.getHTMLComponent());
//
//        locMetric.setFile(file);
//        locMetric.processMetric();
//        log.info("LOCMetricsProcessing: {} ", ccMetric.getHTMLComponent());
//
//        miMetric.setFile(file);
//        miMetric.setMetrics(ccMetric, hmMetric, locMetric);
//        miMetric.processMetric();
//        log.info("MaintainabilityIndexMetricProcessing: {}", miMetric.getHTMLComponent());
//
//        cboMetric.setFile(file);
//        cboMetric.processMetric();
//        cboMetric.printMetric();
//        log.info("CouplingBetweenObjectsMetricProcessing: {}", cboMetric.getHTMLComponent());
//
        try {
            complyWithConventionCheck.setFile(file);
            complyWithConventionCheck.processMetric();
            log.info("ClassComplyWithConvention: {}", complyWithConventionCheck.getHTMLComponent());
        } catch (Exception e){
            log.error("Error: {}", e);
        }

    }
}

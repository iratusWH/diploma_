package main;

import lombok.extern.slf4j.Slf4j;
import metrics.classes.processing.metrics.CouplingBetweenObjectsMetricProcessing;
import metrics.classes.processing.metrics.CyclomaticComplexityMetricProcessing;
import metrics.classes.processing.metrics.DepthOfInheritanceTreeMetricProcessing;
import metrics.classes.processing.metrics.HalsteadMetricsProcessing;
import metrics.classes.processing.metrics.LOCMetricsProcessing;
import metrics.classes.processing.metrics.MaintainabilityIndexMetricProcessing;
import metrics.classes.text.checks.BracketsCheck;
import metrics.classes.text.checks.ClassComplyWithConvention;
import metrics.interfaces.MetricProcessing;
import metrics.interfaces.SimpleMetricProcessing;
import support.classes.ResourceFiles;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * AllMetricsStarter - класс запускающий все метрики по-очереди
 */
@Slf4j
public class AllMetricsStarter {
    ResourceFiles resourceFiles; // объект хранящий список файлов

    private final List<SimpleMetricProcessing> metricList; // список метрик, требующих на вход один файл
    private final DepthOfInheritanceTreeMetricProcessing ditMetric; // метрика глубины дерева наследования
    private final MaintainabilityIndexMetricProcessing miMetric; // метрика ремонтопригодности кода

    // инициализация объектов метрик
    private AllMetricsStarter(String fullProjectPath) {
        resourceFiles = new ResourceFiles(fullProjectPath);

        metricList = Arrays.asList(
                new CouplingBetweenObjectsMetricProcessing(),
                new CyclomaticComplexityMetricProcessing(),
                new HalsteadMetricsProcessing(),
                new LOCMetricsProcessing(),
                new BracketsCheck(),
                new ClassComplyWithConvention()
        );

        miMetric = new MaintainabilityIndexMetricProcessing();
        ditMetric = new DepthOfInheritanceTreeMetricProcessing();
    }

    public static AllMetricsStarter getStarter(String fullProjectPath) {
        return new AllMetricsStarter(fullProjectPath);
    }

    // запуск метрик
    public void execute() {
        if (!resourceFiles.getFileList().isEmpty()) {
            resourceFiles.filterFileList();
            resourceFiles.getFilteredFileList()
                    .forEach(this::doSimpleMetrics); // обработка каждой метрики в цикле

            doComplexMetrics(resourceFiles);
        } else {
            log.warn("Java classes not found!");
        }
    }

    // вычисление метрики требующей всю директорию файлов java
    private void doComplexMetrics(ResourceFiles resourceFiles) {
        ditMetric.setFileList(resourceFiles);
        ditMetric.processMetric();
        printMetric(ditMetric);
    }

    // выполнение каждой метрики, которая требует один файл для анализа
    private void doSimpleMetrics(File file) {
        printDelimiter();
        printDelimiter();
        log.info("File: {}", file.getPath());
        metricList.forEach(metric -> doMetricFabric(file, metric));
        doMaintainabilityMetric(file, metricList);
    }

    private void doMaintainabilityMetric(File file, List<SimpleMetricProcessing> metricList){
        CyclomaticComplexityMetricProcessing ccMetric = (CyclomaticComplexityMetricProcessing) metricList.stream()
                .filter(CyclomaticComplexityMetricProcessing.class::isInstance)
                .findFirst()
                .orElseThrow();

        HalsteadMetricsProcessing hmMetric = (HalsteadMetricsProcessing) metricList.stream()
                .filter(HalsteadMetricsProcessing.class::isInstance)
                .findFirst()
                .orElseThrow();

        LOCMetricsProcessing locMetric = (LOCMetricsProcessing) metricList.stream()
                .filter(LOCMetricsProcessing.class::isInstance)
                .findFirst()
                .orElseThrow();

        miMetric.setMetrics(ccMetric, hmMetric, locMetric);
        setUpMetric(file, miMetric);
        printMetric(miMetric);
    }

    private void doMetricFabric(File file, SimpleMetricProcessing metricClass) {
        setUpMetric(file, metricClass);
        printMetric(metricClass);
    }

    private void setUpMetric(File file, SimpleMetricProcessing metric){
        metric.setFile(file);
        metric.processMetric();
    }

    private void printMetric(MetricProcessing metric){
        printDelimiter();
        log.info("{}: {}", metric.getClass().getSimpleName(), metric.getMetric());
    }

    private void printDelimiter(){
        log.info("=".repeat(50));
    }
}
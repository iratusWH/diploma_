package main;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.oop.antipattern.AnemicDomainModelAntipattern;
import metrics.classes.oop.antipattern.CallSuperAntipattern;
import metrics.classes.oop.antipattern.GodObjectAntipattern;
import metrics.classes.processing.metrics.CouplingBetweenObjectsMetricProcessing;
import metrics.classes.processing.metrics.CyclomaticComplexityMetricProcessing;
import metrics.classes.processing.metrics.DepthOfInheritanceTreeMetricProcessing;
import metrics.classes.processing.metrics.HalsteadMetricsProcessing;
import metrics.classes.processing.metrics.LOCMetricsProcessing;
import metrics.classes.processing.metrics.MaintainabilityIndexMetricProcessing;
import metrics.classes.text.checks.BracketsCheck;
import metrics.classes.text.checks.ClassComplyWithNamingConventionCheck;
import metrics.classes.text.checks.VariableOnNewLineCheck;
import metrics.interfaces.MetricProcessing;
import metrics.interfaces.SimpleMetricProcessing;
import support.classes.HTMLComponent;
import support.classes.HTMLComposer;
import support.classes.ResourceFiles;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * StaticAnalyzer
 * <p>
 * AllMetricsStarter - класс запускающий все метрики по-очереди
 *
 * @author Маркелов Александр A-07-18
 */
@Slf4j
public class AllMetricsStarter {
    ResourceFiles resourceFiles; // объект хранящий список файлов

    private final List<SimpleMetricProcessing> metricList; // список метрик, требующих на вход один файл
    private final DepthOfInheritanceTreeMetricProcessing ditMetric; // метрика глубины дерева наследования
    private final MaintainabilityIndexMetricProcessing miMetric; // метрика ремонтопригодности кода
    private final List<HTMLComponent> metricComponentList = new ArrayList<>(40);

    // инициализация объектов метрик
    private AllMetricsStarter(String fullProjectPath) {
        resourceFiles = new ResourceFiles(fullProjectPath);

        metricList = Arrays.asList(
                new CouplingBetweenObjectsMetricProcessing(),
                new CyclomaticComplexityMetricProcessing(),
                new HalsteadMetricsProcessing(),
                new LOCMetricsProcessing(),
                new BracketsCheck(),
                new ClassComplyWithNamingConventionCheck(),
//                new GodObjectAntipattern(),
                new VariableOnNewLineCheck()
//                new AnemicDomainModelAntipattern()
        );

        miMetric = new MaintainabilityIndexMetricProcessing();
        ditMetric = new DepthOfInheritanceTreeMetricProcessing();
    }

    public static AllMetricsStarter getStarter(String fullProjectPath) {
        return new AllMetricsStarter(fullProjectPath);
    }

    // запуск метрик
    public String execute() {
        if (!resourceFiles.getFileList().isEmpty()) {
            resourceFiles.getCompilationUnitList()
                    .forEach(this::doSimpleMetrics); // обработка каждой метрики в цикле

            doComplexMetrics(resourceFiles);
            try {
                composeHtml();
            } catch (IOException e) {
                log.error("Problems wih saving HTML doc");
            }
            return "OK";
        } else {
            return "Java classes not found!";
        }
    }

    // вычисление метрики требующей всю директорию файлов java
    private void doComplexMetrics(ResourceFiles resourceFiles) {
        ditMetric.setFileList(resourceFiles);
        ditMetric.processMetric();
        printMetric(ditMetric);

        metricComponentList.add(ditMetric.getHtmlComponent());
    }

    // выполнение каждой метрики, которая требует один файл для анализа
    private void doSimpleMetrics(CompilationUnit file) {
        printDelimiter();
        printDelimiter();

        log.info("File: {}",
                file.getPackageDeclaration()
                        .map(PackageDeclaration::getName)
                        .map(Name::asString)
                        .map(name -> name + "." + file.findFirst(ClassOrInterfaceDeclaration.class)
                                .map(ClassOrInterfaceDeclaration::getNameAsString)
                                .orElseThrow()
                        )
                        .orElseThrow()
        );
        metricList.forEach(metric -> doMetricFabric(file, metric));
        doMaintainabilityMetric(file, metricList);

        metricComponentList.addAll(
                metricList
                        .stream()
                        .map(SimpleMetricProcessing::getHtmlComponent)
                        .toList()
        );

        metricComponentList.add(miMetric.getHtmlComponent());
    }

    private void doMaintainabilityMetric(CompilationUnit file, List<SimpleMetricProcessing> metricList) {
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

    private void doMetricFabric(CompilationUnit file, SimpleMetricProcessing metricClass) {
        setUpMetric(file, metricClass);
        printMetric(metricClass);
    }

    private void composeHtml() throws IOException {
        HTMLComposer composer = new HTMLComposer(resourceFiles.getProjectPath());
        composer.setHtmlComponents(metricComponentList);
        composer.composeHtmlByTemplate(new File("src/main/resources/Template.html"));
    }

    private void setUpMetric(CompilationUnit file, SimpleMetricProcessing metric) {
        metric.setFile(file);
        metric.processMetric();
    }


    private void printMetric(MetricProcessing metric) {
        printDelimiter();
        log.info("{}: {}", metric.getClass().getSimpleName(), metric.getMetric());
    }

    private void printDelimiter() {
        log.info("=".repeat(50));
    }
}
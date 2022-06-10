package metrics.classes.implementations;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.expr.Name;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.interfaces.MetricProcessing;
import metrics.interfaces.SimpleMetricProcessing;
import support.classes.HTMLComponent;
import support.classes.MetricNameEnum;

/**
 * Класс обычной метрики, исследуемая метрика основана на одном файле
 */
@Slf4j
@Data
public abstract class SimpleMetricProcessingImpl implements SimpleMetricProcessing, MetricProcessing {

    private Object metric; // результирующая метрика
    private MetricNameEnum metricName; // название метрики
    private CompilationUnit file; // исследумый файл

    @Override
    public HTMLComponent getHtmlComponent(){
        return HTMLComponent.builder()
                .directoryAndFileName(file.getPackageDeclaration()
                        .map(PackageDeclaration::getName)
                        .map(Name::asString)
                        .map(name -> name + "." + file.findFirst(ClassOrInterfaceDeclaration.class)
                                .map(ClassOrInterfaceDeclaration::getNameAsString)
                                .orElseThrow()
                        )
                        .orElseThrow())
                .metricName(metricName)
                .metric(metric.toString())
                .build();
    }
}

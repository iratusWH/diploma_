package metrics.classes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.TypeDeclaration;
import lombok.Data;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;

@Data
@Slf4j
public class HalsteadMetricsProcessing extends MetricProcessingImpl {

    // Total metrics
    public int distinctOperators;
    public int distinctOperands;
    public int totalOperators;
    public int totalOperands;

    // Metrics for only class
    private final int vocabulary = 0;
    private final int programLength = 0;
    private final double calcProgramLength = 0;
    private final double volume = 0;
    private final double difficulty = 0;
    private final double effort = 0;
    private final double timeReqProgram = 0;
    private final double timeDelBugs = 0;

    private CompilationUnit compilationUnit;

    public HalsteadMetricsProcessing() {
        distinctOperands = 0;
        distinctOperators = 0;
        totalOperands = 0;
        totalOperators = 0;
    }

    @Override
    public void processMetric() {
        try {
            compilationUnit = StaticJavaParser.parse(getFile());

            Optional<TypeDeclaration<?>> declaration = compilationUnit.getPrimaryType();
            System.out.println(declaration.map(TypeDeclaration::getName));
        } catch (IOException exception){
            System.out.println("Some problem");
        }
    }

    @Override
    public void preprocessOutput() {

    }

}

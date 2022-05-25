package metrics.classes.processing.metrics;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.ComplexMetricProcessingImpl;
import support.classes.MetricNameEnum;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class DepthOfInheritanceTreeMetricProcessing extends ComplexMetricProcessingImpl {

    private int maxDepth;
    private Map<String, Integer> depthOfInheritanceMap;

    public DepthOfInheritanceTreeMetricProcessing() {
        setMetricName(MetricNameEnum.DEPTH_OF_INHERITANCE_METRIC);
    }

    @Override
    public void processMetric() {
        depthOfInheritanceMap = new HashMap<>();
        getFileList().filterFileList();
        List<File> classFiles = getFileList().getFilteredFileList();
        try {
            setMetric(String.valueOf(searchMaxDepthOfInheritance(classFiles)));
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("File not found!");
        }
    }

    public int searchMaxDepthOfInheritance(List<File> classFiles) throws FileNotFoundException {
        int depth = 0;
        int tempDepth;
        for (File classFile : classFiles) {
            tempDepth = visitor(classFile);
            if (tempDepth > depth) {
                depth = tempDepth;
            }
        }
        return depth;
    }

    public int visitor(File classFile) throws FileNotFoundException {
        int temporaryIndex;
        compilationUnit = StaticJavaParser.parse(classFile);

        ClassOrInterfaceDeclaration clazz = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow();
        String classFileName = clazz.getNameAsString();

        if (clazz.getExtendedTypes().isEmpty()) {
            depthOfInheritanceMap.put(classFileName, 0);
            return 0;
        }
        if (depthOfInheritanceMap.containsKey(clazz.getExtendedTypes().toString())) {
            return depthOfInheritanceMap.get(classFileName) + 1;
        }

        String parent = clazz.getExtendedTypes().stream().findFirst()
                .map(NodeWithSimpleName::getNameAsString)
                .orElseThrow();

        File parentClassFile = getFileList().getFileByName(parent + ".java");

        temporaryIndex = 1 + visitor(parentClassFile);
        depthOfInheritanceMap.put(classFileName, temporaryIndex);

        return temporaryIndex;
    }
}

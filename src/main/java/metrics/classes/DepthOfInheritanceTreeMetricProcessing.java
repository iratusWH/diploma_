package metrics.classes;

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
    private Map<String, Integer> depthOfInheritance;
    private String className;

    public DepthOfInheritanceTreeMetricProcessing(){
        setMetricName(MetricNameEnum.DEPTH_OF_INHERITANCE_METRIC);
    }

    @Override
    public void processMetric() {
        depthOfInheritance = new HashMap<>();
        getFileList().filterFileList();
        List<File> classFiles = getFileList().getFilteredFileList();
        try {
            setMetric(String.valueOf(searchMaxDepthOfInheritance(classFiles)));
            setFileName(className);
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("File not found!");
        }

    }

    public int searchMaxDepthOfInheritance(List<File> classFiles) throws FileNotFoundException{
        int depth = 0;
        int tempDepth;
        for (File classFile : classFiles) {
            tempDepth = visitor(classFile);
            log.info("File - {}; Inheritance tree - {}", classFile.getName(), tempDepth);
            if (tempDepth > depth){
                depth = tempDepth;
                className = classFile.getPath();
            }
        }
        return depth;
    }

    public int visitor(File classFile) throws FileNotFoundException {
        int temporaryIndex;
        compilationUnit = StaticJavaParser.parse(classFile);

        ClassOrInterfaceDeclaration clazz = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow();
        String className = clazz.getNameAsString();
        depthOfInheritance.put(className, 0);

        if (clazz.getExtendedTypes().isEmpty()) {
            depthOfInheritance.put(className, 0);
            return 0;
        }
        if (depthOfInheritance.containsKey(clazz.getExtendedTypes().toString())) {
            return depthOfInheritance.get(className) + 1;
        }

        String parent = clazz.getExtendedTypes().stream().findFirst()
                .map(NodeWithSimpleName::getNameAsString)
                .orElseThrow();

        File parentClassFile = getFileList().getFileByName(parent + ".java");

        temporaryIndex = 1 + visitor(parentClassFile);
        depthOfInheritance.put(parent, temporaryIndex);

        return temporaryIndex;
    }
}

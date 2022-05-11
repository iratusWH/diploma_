package metrics.classes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.ComplexMetricProcessingImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class DepthOfInheritanceTreeMetricProcessing extends ComplexMetricProcessingImpl {

    private int maxDepth;
    private Map<String, Integer> depthOfInheritance;
    private static final String DEPTH_OF_INHERITANCE_TREE = "Depth of inheritance tree";

    @Override
    public void processMetric() {
        depthOfInheritance = new HashMap<>();
        int tempDepth;
        int depth = 0;

        getFileList().filterFileList();
        List<File> classFiles= getFileList().getFilteredFileList();
        try {
            for (File classFile : classFiles) {
                tempDepth = visitor(classFile);
                log.info("Inheritance tree {}", tempDepth);
                depth = Math.max(tempDepth, depth);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("File not found!");
        }

        maxDepth = depth;

        preprocessOutput();
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

        File parentClassFile = getFileList().getFileByName(parent);

        temporaryIndex = 1 + visitor(parentClassFile);
        depthOfInheritance.put(parent, temporaryIndex);

        return temporaryIndex;
    }

    @Override
    public void preprocessOutput() {
        String metricResult = DEPTH_OF_INHERITANCE_TREE.concat(" = ").concat(Integer.toString(maxDepth));

        setMetric(metricResult);
    }

    @Override
    public void preprocessHTML() {

    }
}

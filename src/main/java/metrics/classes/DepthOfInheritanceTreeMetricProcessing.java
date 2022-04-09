package metrics.classes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.ComplexMetricProcessingImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        filterFileList();
        try {
            for (File classFile : getFileList()) {
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
        int temp;
        compilationUnit = StaticJavaParser.parse(classFile);

        ClassOrInterfaceDeclaration clazz = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow();
        String className = clazz.getNameAsString();
        depthOfInheritance.put(className, 0);

        if (clazz.getExtendedTypes().isEmpty()) {
            depthOfInheritance.put(className, 0);
            return 0;
        }
        if (depthOfInheritance.containsKey(className)) {
            return depthOfInheritance.get(className);
        }

        String parent = clazz.getExtendedTypes().stream().findFirst()
                .map(NodeWithSimpleName::getNameAsString)
                .orElseThrow();

        File parentClassFile = getFileList().stream().filter(
                        parentClass -> parentClass.toString().contains(parent))
                .toList()
                .get(0);

        temp = 1 + visitor(parentClassFile);
        depthOfInheritance.put(parent, temp);

        return temp;
    }

    private void filterFileList() {
        List<File> filteredList = getFileList().stream()
                .filter(
                        file -> {
                            try {
                                compilationUnit = StaticJavaParser.parse(file);
                                return !compilationUnit.findFirst(ClassOrInterfaceDeclaration.class)
                                        .orElseThrow()
                                        .isInterface();
                            } catch (FileNotFoundException e) {
                                log.error("Error while filtering list: ", e);
                            }
                            return false;
                        })
                .toList();

        setFileList(filteredList);
    }

    @Override
    public void preprocessOutput() {
        String metricResult = DEPTH_OF_INHERITANCE_TREE.concat(" = ").concat(Integer.toString(maxDepth));

        setMetric(metricResult);
    }
}

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
import java.util.Objects;

/**
 * Класс вычисления максимальной глубины дерева наследования
 */
@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public class DepthOfInheritanceTreeMetricProcessing extends ComplexMetricProcessingImpl {

    private int maxDepth; // максимальная глубина дерева наследования
    private Map<String, Integer> depthOfInheritanceMap; // словарь имен классов и уровня наследования

    public DepthOfInheritanceTreeMetricProcessing() {
        setMetricName(MetricNameEnum.DEPTH_OF_INHERITANCE_METRIC); // ввод названия метрики
    }

    @Override
    public void processMetric() {
        depthOfInheritanceMap = new HashMap<>();
        getFileList().filterFileList(); // фильтрация листа с исследуемыми файлами, остаются только файлы содержащие классы
        try {
            setMetric( // вывод метрики
                    String.valueOf(
                            searchMaxDepthOfInheritance(getFileList().getFilteredFileList()) // поиск максимальной глубины
                    )
            );
            log.info("{}", depthOfInheritanceMap);
        } catch (FileNotFoundException fileNotFoundException) {
            log.error("File not found!");
        }
    }

    public int searchMaxDepthOfInheritance(List<File> classFiles) throws FileNotFoundException {
        int depth = 0; // максимальная глубина
        int tempDepth; // временная глубина
        for (File classFile : classFiles) {
            tempDepth = visitClass(classFile); // находение глубины наследования текущего файла
            if (tempDepth > depth) {
                depth = tempDepth;
            }
        }
        return depth;
    }

    public int visitClass(File classFile) throws FileNotFoundException {
        int temporaryIndex;
        compilationUnit = StaticJavaParser.parse(classFile); // получаем разобранный файл для анализа

        ClassOrInterfaceDeclaration clazz = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class).orElseThrow(); // поиск первого входения класса в файле
        String classFileName = clazz.getNameAsString(); // название класса

        if (clazz.getExtendedTypes().isEmpty()) { // если после extends нет класса, то записываем нуль в словарь
            depthOfInheritanceMap.put(classFileName, 0);
            return 0;
        }
        if (depthOfInheritanceMap.containsKey(clazz.getExtendedTypes().toString())) { // если в словаре уже есть класс-родитель, то добавляем 1 и кладем в словарь
            return depthOfInheritanceMap.get(clazz.getExtendedTypes().toString()) + 1;
        }

        String parent = clazz.getExtendedTypes().stream().findFirst()
                .map(NodeWithSimpleName::getNameAsString)
                .orElseThrow(); // получение наименования класса-родителя

        File parentClassFile = getFileList().getFileByName(parent + ".java"); // поиск класса-родителя в листе
        if (Objects.isNull(parentClassFile)) {
            depthOfInheritanceMap.put(classFileName, 1); // если файла нет в директории
            return 1;
        }

        temporaryIndex = 1 + visitClass(parentClassFile); // если файла есть в директории
        depthOfInheritanceMap.put(classFileName, temporaryIndex);
        return temporaryIndex;
    }
}

import metrics.classes.CyclomaticComplexityMetricProcessing;
import support.classes.ToolBox;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static support.classes.ToolBox.dirList;

public class Main {
    public static void main(String[] args) {

        List<File> filesPaths = dirList("/home/xela/IdeaProjects/diploma/src/main/java");

        List<String> paths = filesPaths
                .stream()
                .map(File::toString)
                .toList();

        System.out.println(
                paths
        );

        CyclomaticComplexityMetricProcessing ccMetric = new CyclomaticComplexityMetricProcessing();
        ccMetric.processMetric(filesPaths.get(0).toPath());
        System.out.println(ccMetric.getMetric());

//        System.out.println(
//                paths.stream().map(ToolBox::readFile).collect(Collectors.toList())
//        );
    }
}


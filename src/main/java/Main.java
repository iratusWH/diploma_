import metrics.classes.CyclomaticComplexityMetricProcessing;

import java.io.File;
import java.util.List;

import static support.classes.ToolBox.dirList;

public class Main {
    public static void main(String[] args) {

        List<File> filesPaths = dirList("/home/xela/IdeaProjects/diploma/src/main/java");

        List<String> paths = filesPaths
                .stream()
                .map(File::toString)
                .toList();

        CyclomaticComplexityMetricProcessing ccMetric = new CyclomaticComplexityMetricProcessing();
        filesPaths.forEach(file -> {
                    ccMetric.processMetric(file.toPath());
                    ccMetric.printMetric();
                }
        );
    }
}


import support.classes.ToolBox;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static support.classes.ToolBox.dirList;
import static support.classes.ToolBox.readFile;

public class Main {
    public static void main(String[] args) {

        List<String> paths = dirList("/home/xela/IdeaProjects/diploma/src/main/java")
                .stream()
                .map(File::toString)
                .toList();

        System.out.println(
                paths
        );

        System.out.println(
                paths.stream().map(ToolBox::readFile).collect(Collectors.toList())
        );
    }
}


package support.classes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ToolBox {

    public static List<File> dirList(String startDirectory) {
        List<File> javaFilesList = new ArrayList<>();
        List<Path> directoryList = new ArrayList<>();
        List<Path> directorySecondList = new ArrayList<>();

        Path startDirectoryPath = Path.of(startDirectory);
        File directoryFile;
        boolean isNotEndOfDirectories = true;

        try {
            Files.newDirectoryStream(startDirectoryPath)
                    .forEach(directoryList::add);
        } catch (IOException ignored) {
            System.out.println("ToolBox -> dirList(startDirectory): Error while getting directories");
        }

        int depth = 0;
        while (isNotEndOfDirectories) {
            for (Path filePath : directoryList) {

                directoryFile = Optional.of(filePath)
                        .map(Path::toFile)
                        .orElse(null);

                if (directoryFile.isFile()) {
                    javaFilesList.add(directoryFile);
                }

                if (directoryFile.isDirectory()) {
                    directorySecondList.addAll(
                            Arrays.stream(
                                    Objects.requireNonNull(
                                            filePath.toFile()
                                                    .listFiles())
                                    )
                                    .map(File::toPath)
                                    .toList()
                    );
                }
                System.out.println("-".repeat(3 * depth) + "| " + filePath);
            }
            directoryList.clear();
            directoryList.addAll(directorySecondList);
            isNotEndOfDirectories = !directoryList.isEmpty();
            directorySecondList.clear();

            ++depth;
        }

        return javaFilesList;
    }

    public static String readFile(String path){
        File file = new File(path);
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(file)
            );

            String st;
            StringBuilder textFile = new StringBuilder();

            while ((st = br.readLine()) != null)
                textFile.append("\n").append(st);

            return textFile.toString();
        } catch (IOException ignored){
            return "";
        }
    }

}

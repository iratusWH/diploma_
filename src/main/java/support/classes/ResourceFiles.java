package support.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Data
public class ResourceFiles {

    private String projectPath;
    private List<File> fileList;

    public ResourceFiles(String projectPath){
        this.projectPath = projectPath;
        setFileListByDirectory(projectPath);
    }

    public File getFileByName(String fileName){
        return fileList.stream()
                .filter(
                        file -> file.toString().contains(fileName))
                .findFirst()
                .orElseThrow();
    }

    public void setFileListByDirectory(String startDirectory) {
        List<File> javaFilesList = new ArrayList<>();
        List<Path> directoryList = new ArrayList<>();
        List<Path> directorySecondList = new ArrayList<>();

        projectPath = startDirectory;

        Path startDirectoryPath = Path.of(startDirectory);
        File directoryFile;
        boolean isNotEndOfDirectories = true;

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(startDirectoryPath)){
                    directoryStream.forEach(directoryList::add);
        } catch (IOException ignored) {
            log.error("ToolBox -> dirList(startDirectory): Error while getting directories");
        }

        while (isNotEndOfDirectories) {
            for (Path filePath : directoryList) {

                try {
                    directoryFile = Optional.of(filePath)
                            .map(Path::toFile)
                            .orElseThrow(
                                    FileNotFoundException::new
                            );

                    // предполагается, что файл с расширением ".java" является Java файлом
                    if (directoryFile.toString().toLowerCase(Locale.ROOT).contains(".java")) {
                        javaFilesList.add(directoryFile);
                    }

                    // если файл является директорией, то записываем его в лист директорий
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
                } catch (FileNotFoundException fileNotFoundException){
                    log.error("Invalid file: ", fileNotFoundException);
                }
            }
            // очищаем лист для загрузки листа с собранными данными
            directoryList.clear();
            directoryList.addAll(directorySecondList);

            // проверка на пустоту листа на наличие файлов в директории
            isNotEndOfDirectories = !directoryList.isEmpty();
            directorySecondList.clear();
        }

        fileList = javaFilesList;
    }

}

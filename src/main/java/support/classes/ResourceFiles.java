package support.classes;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

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

/**
 * Вспомогательный класс хранящий в себе список файлов проекта
 */
@Slf4j
@Data
public class ResourceFiles {

    private final String projectPath; // строка пути проекта
    private List<File> fileList; // список всех файлов проекта
    private List<File> filteredFileList; // отфильтрованный список java-классов
    private CompilationUnit compilationUnit;
    private List<CompilationUnit> compilationUnitList; // список подготовленных файлов

    public ResourceFiles(String projectPath) {
        this.projectPath = projectPath;
        setFileListByDirectory(projectPath);
    }

    /**
     * получение объекта класса File из строки-директории
     */
    public File getFileByName(String fileName) {
        return fileList.stream()
                .filter(file -> fileName.equalsIgnoreCase(file.getName()))
                .findFirst()
                .orElse(null);
    }

    /**
     * установка списка файлов Java
     */
    public void setFileListByDirectory(String startDirectory) {
        List<File> javaFilesList = new ArrayList<>();
        List<Path> directoryList = new ArrayList<>();
        List<Path> directorySecondList = new ArrayList<>();

        Path startDirectoryPath = Path.of(startDirectory);
        File directoryFile;
        boolean isNotEndOfDirectories = true;

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(startDirectoryPath)) {
            directoryStream.forEach(directoryList::add);
        } catch (IOException ignored) {
            log.error("setFileListByDirectory: Error while getting directories");
        }

        while (isNotEndOfDirectories) {
            for (Path filePath : directoryList) {

                try {
                    directoryFile = Optional.of(filePath).map(Path::toFile).orElseThrow(FileNotFoundException::new);

                    // предполагается, что файл с расширением ".java" является Java классом/интерфейсом/перечислением
                    if (directoryFile.toString().toLowerCase(Locale.ROOT).contains(".java")) {
                        javaFilesList.add(directoryFile);
                    }

                    // если файл является директорией, то записываем его в лист директорий
                    if (directoryFile.isDirectory()) {
                        directorySecondList.addAll(
                                Arrays.stream(Objects.requireNonNull(filePath.toFile().listFiles()))
                                .map(File::toPath)
                                .toList()
                        );
                    }
                } catch (FileNotFoundException fileNotFoundException) {
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

        setFileList(javaFilesList);
        filterFileList();
        parseFileList();
    }

    /**
     * Фильтрация списка файлов
     */
    public void filterFileList() {
        setFilteredFileList(
                getFileList()
                .stream()
                .filter(this::isFileClass)
                .toList()
        );
    }

    /**
     * Подготовка списка файлов для анализа
     */
    public void parseFileList(){
        setCompilationUnitList(
                getFilteredFileList()
                        .stream()
                        .map(this::getParsedFile)
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    private boolean isFileClass(File file) {
        try {
            return isJavaClassFile(StaticJavaParser.parse(file));
        } catch (FileNotFoundException e) {
            log.error("ResourceFiles error while filtering list: ", e);
        }
        return false;
    }

    /**
     * Проверка на соответствие Java классам
     */
    private boolean isJavaClassFile(CompilationUnit compilationUnit) {
        return compilationUnit.findFirst(ClassOrInterfaceDeclaration.class, element -> !isEnumInterfaceOrRecordDeclaration(element)).isPresent();
    }

    /**
     * Проверка на соответствие перечисляемым типам Java
     */
    private boolean isEnumInterfaceOrRecordDeclaration(ClassOrInterfaceDeclaration declaration) {
        return declaration.isEnumDeclaration() || declaration.isInterface() || declaration.isEnumConstantDeclaration() || declaration.isRecordDeclaration();
    }

    private CompilationUnit getParsedFile(File javaFile){
        try {
            return StaticJavaParser.parse(javaFile);
        } catch (Exception e) {
            log.error("Cannot parse file: {}", javaFile.getName());
        }

        return null;
    }
}

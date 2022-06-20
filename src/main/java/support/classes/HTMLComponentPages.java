package support.classes;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HTMLComponentPages {
    private static final String DIV_METRIC_WRAPPER = "<div class='metric_page_wrapper'>";
    private static final String DIV_STARTING_CLASS_FILE_PATH = "<div class='file_path'>";
    private static final String DIV_ENDING = "</div>\n";

    @Getter List<String> pages;

    public HTMLComponentPages(List<HTMLComponent> componentList) {
        pages = new ArrayList<>();
        composePages(componentList);
    }

    private void composePages(List<HTMLComponent> componentList) {
        componentList
                .stream()
                .collect(Collectors.groupingBy(HTMLComponent::getDirectoryAndFileName))
                .forEach((key, value) -> pages.add(composeSinglePage(key, value)));

        Collections.sort(pages);
    }

    private String composeSinglePage(String filePath, List<HTMLComponent> singleFileComponents) {
        return DIV_METRIC_WRAPPER +
                    DIV_STARTING_CLASS_FILE_PATH +
                        filePath + "\n" +
                    DIV_ENDING +
                        singleFileComponents
                                .stream()
                                .map(HTMLComponent::composeHTML)
                                .collect(Collectors.joining("\n")) +
                DIV_ENDING;
    }

}

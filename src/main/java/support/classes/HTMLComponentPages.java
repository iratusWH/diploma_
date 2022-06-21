package support.classes;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class HTMLComponentPages {
    private static final String DIV_METRIC_WRAPPER_OUTER = "<div class='metric_page_wrapper_outer'>";
    private static final String DIV_METRIC_WRAPPER_INNER = "<div class='metric_page_wrapper_inner'>";
    private static final String DIV_STARTING_CLASS_FILE_PATH = "<div class='file_path'>";
    private static final String DIV_PAGE = "<div class='page'>";
    private static final String HEADER_OPEN = "<h3>";
    private static final String HEADER_CLOSE = "</h3>\n";
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
        return DIV_METRIC_WRAPPER_OUTER +
                    DIV_METRIC_WRAPPER_INNER +
                        DIV_STARTING_CLASS_FILE_PATH +
                            HEADER_OPEN +
                                "File path: " + filePath + "\n" +
                            HEADER_CLOSE +
                        DIV_ENDING +
                        DIV_PAGE +
                            singleFileComponents
                                    .stream()
                                    .map(HTMLComponent::composeHTML)
                                    .collect(Collectors.joining("\n")) +
                        DIV_ENDING +
                    DIV_ENDING +
                DIV_ENDING;
    }

}

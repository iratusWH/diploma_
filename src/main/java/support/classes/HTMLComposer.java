package support.classes;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class HTMLComposer {

    List<HTMLComponent> htmlComponents = new ArrayList<>();

    private static final String DIV_STARTING_METRIC_NAME = "<div class='metric_name'>";
    private static final String DIV_STARTING_CLASS_FILE_PATH = "<div class='file_path'>";
    private static final String DIV_STARTING_CLASS_METRIC_RESULT = "<div class='metric_result'>";
    private static final String DIV_ENDING = "</div>\n";

    private final String path;

    HTMLComposer(String path){
        this.path = path + "MetricResult.html";
    }

    void composeHtmlByTemplate(File template) throws IOException {

        log.info("HTMLCompiler {}", template);

        Document htmlDoc = Jsoup.parse(template, "UTF-8");

        htmlComponents.forEach(component -> composeHTMLByComponent(htmlDoc, component));

        log.info("HTMLCompiler {}", htmlDoc);
        try {
            generateOutputFileByHTMLDocument(htmlDoc);
        } catch (Exception e){
            log.error("HTMLComposer error while generating output html file; {}", e.getMessage());
            throw e;
        }
        log.info("HTMLComposer generation successfully completed");
    }

    void composeHTMLByComponent(Document htmlDoc, HTMLComponent component){
        htmlDoc.body().append(DIV_STARTING_METRIC_NAME + component.getMetricName().getName())
                .append(DIV_STARTING_CLASS_FILE_PATH + component.getDirectoryAndFileName() + DIV_ENDING)
                .append(DIV_STARTING_CLASS_METRIC_RESULT + component.getMetric() + DIV_ENDING + DIV_ENDING);
    }

    void generateOutputFileByHTMLDocument(Document htmlDoc) throws IOException {
        FileUtils.writeStringToFile(new File(path), htmlDoc.outerHtml(), StandardCharsets.UTF_8);
    }
}

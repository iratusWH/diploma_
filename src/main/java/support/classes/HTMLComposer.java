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

    private List<HTMLComponent> htmlComponents = new ArrayList<>();

    private final String path;

    public HTMLComposer(String path){
        this.path = path + "MetricResult.html";
    }

    public void composeHtmlByTemplate(File template) throws IOException {

        log.info("HTMLCompiler {}", template);

        Document htmlDoc = Jsoup.parse(template, "UTF-8");

        HTMLComponentPages pages = new HTMLComponentPages(htmlComponents);
        pages.getPages()
                .forEach(page -> htmlDoc.body().getElementsByTag("wrapper").append(page));

        log.info("HTMLCompiler {}", htmlDoc);
        try {
            generateOutputFileByHTMLDocument(htmlDoc);
        } catch (Exception e){
            log.error("HTMLComposer error while generating output html file; {}", e.getMessage());
            throw e;
        }
        log.info("HTMLComposer generation successfully completed");
    }

    void generateOutputFileByHTMLDocument(Document htmlDoc) throws IOException {
        FileUtils.writeStringToFile(new File(path), htmlDoc.outerHtml(), StandardCharsets.UTF_8);
    }
}

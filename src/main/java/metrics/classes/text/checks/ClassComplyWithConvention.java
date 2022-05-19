package metrics.classes.text.checks;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.nodeTypes.NodeWithSimpleName;
import lombok.extern.slf4j.Slf4j;
import metrics.classes.implementations.MetricProcessingImpl;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.regex.Pattern;

import static support.classes.RegexPatterns.*;

@Slf4j
public class ClassComplyWithConvention extends MetricProcessingImpl {
    public static final String OK_MESSAGE = "OK";

    Pattern patternForClassOrInterface = Pattern.compile(CLASS_OR_INTERFACE_DECLARATION_TEMPLATE);
    Pattern patternForVariable = Pattern.compile(VARIABLE_DECLARATION_TEMPLATE);
    Pattern patternForConst = Pattern.compile(CONST_DECLARATION_TEMPLATE);

    @Override
    public void processMetric() throws FileNotFoundException {
        try{
            CompilationUnit compilationUnit = StaticJavaParser.parse(getFile());
            List<String> listOfMismatch = compilationUnit.findAll(ClassOrInterfaceDeclaration.class)
                    .stream().map(NodeWithSimpleName::getNameAsString)
                    .filter(nameAsString -> !patternForClassOrInterface.matcher(nameAsString).find())
                    .toList();

            setMetric(listOfMismatch.isEmpty() ? OK_MESSAGE : String.join(", ", listOfMismatch));
        } catch (Exception e) {
            log.error("ClassComplyWithConvention error - {}", e.getMessage());
        }
    }
}

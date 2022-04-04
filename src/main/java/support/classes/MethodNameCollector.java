package support.classes;

import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class MethodNameCollector extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(MethodDeclaration md, List<String> collector){
        super.visit(md, collector);
        collector.add("Name: " + md.getNameAsString() + "; Params: " + md.getParameters() + "; Type: " + md.getTypeAsString());
        System.out.println("Name: " + md.getNameAsString() + "; Params: " + md.getParameters() + "; Type: " + md.getTypeAsString());
    }
}

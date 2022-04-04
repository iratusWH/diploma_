import com.github.javaparser.*;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import support.classes.MethodNameCollector;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        CompilationUnit cu = StaticJavaParser.parse(
                new File("/home/xela/IdeaProjects/diploma/src/main/java/Main.java")
        );

        Optional<ClassOrInterfaceDeclaration> declaration = cu.getClassByName("Main");
        Optional<ClassOrInterfaceDeclaration> declaration1 = cu.getInterfaceByName("NewInterface");

        System.out.println(declaration1);

        List<String> collector = new ArrayList<>();
        MethodNameCollector methodNameCollector = new MethodNameCollector();
        //methodNameCollector.visit(declaration1.get(), collector);
        methodNameCollector.visit(declaration.get(), collector);

        System.out.println(collector);
        System.out.println("Hello, world!");
    }

    public void getSomething(){

    }

    public void getSomething(String str){

    }
}


package testCases;

import com.github.javaparser.ast.body.MethodDeclaration;

public class MethodDeclarationBuilder extends MethodDeclaration {
    MethodDeclaration methodDeclaration = new MethodDeclaration();

    @Override
    public MethodDeclarationBuilder clone() {
        MethodDeclarationBuilder clone = (MethodDeclarationBuilder) super.clone();
        return clone;
    }
}

package support.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OperatorsConstClass {
    public static final String SPACE_STRING = " ";
    public static final String EMPTY_STRING = "";

    public static final List<String> OPERATORS_KEYWORDS_LIST = Arrays.asList(
            "abstract",
            "continue",
            "for",
            "new",
            "switch",
            "assert",
            "default",
            "goto",
            "package",
            "synchronized",
            "boolean",
            "do",
            "if",
            "private",
            "this",
            "break",
            "double",
            "implements",
            "protected",
            "throw",
            "byte",
            "else",
            "import",
            "public",
            "throws",
            "case",
            "enum",
            "instanceof",
            "return",
            "transient",
            "catch",
            "extends",
            "int",
            "short",
            "try",
            "char",
            "final",
            "interface",
            "static",
            "void",
            "class",
            "finally",
            "long",
            "strictfp",
            "volatile",
            "const",
            "float",
            "native",
            "super",
            "while",
            "null"
    );

    public static final List<String> OPERATORS_SEPARATORS_LIST = Arrays.asList(
            "(",
            ")",
            "{",
            "}",
            "[",
            "]",
            ";",
            ",",
            ".",
            "...",
            "@",
            "::"
    );

    public static final List<String> OPERATORS_LIST = Arrays.asList(
            "=",
            ">",
            "<",
            "!",
            "~",
            "?",
            ":",
            "->",
            "==",
            ">=",
            "<=",
            "!=",
            "&&",
            "||",
            "++",
            "--",
            "+",
            "-",
            "*",
            "/",
            "&",
            "|",
            "^",
            "%",
            "<<",
            ">>",
            ">>>",
            "+=",
            "-=",
            "*=",
            "/=",
            "&=",
            "|=",
            "^=",
            "%=",
            "<<=",
            ">>=",
            ">>>="
    );

    public final List<String> ALL_OPERATORS_LIST;
    {
        ALL_OPERATORS_LIST = new ArrayList<>(OPERATORS_LIST);
        ALL_OPERATORS_LIST.addAll(OPERATORS_KEYWORDS_LIST);
        ALL_OPERATORS_LIST.addAll(OPERATORS_SEPARATORS_LIST);
    }
}

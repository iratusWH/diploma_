package support.classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public static final List<String> ALL_OPERATORS_LIST = Stream.of(
            OPERATORS_LIST,
            OPERATORS_KEYWORDS_LIST,
            OPERATORS_SEPARATORS_LIST)
            .flatMap(Collection::stream)
            .toList();

    public static final String JAVADOC_OR_MULTILINE_COMMENT = "/*";
    public static final String ONE_LINE_COMMENT = "//";
    public static final List<String> ESCAPE_SEQUENCES = Arrays.asList(
            "\t",
            "\n",
            "\b",
            "\r",
            "\f",
            "'",
            "\\"
    );

}

package support.classes;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс констант со различными видами скобок, спец-символов, ключевых слов и операторов Java
 */
public class OperatorsConstClass {

    private OperatorsConstClass(){}

    public static final String SPACE_STRING = " ";
    public static final String EMPTY_STRING = "";
    public static final String OPEN_ROUND_BRACKET = "(";
    public static final String CLOSE_ROUND_BRACKET = ")";
    public static final String OPEN_FIGURE_BRACKET = "{";
    public static final String CLOSE_FIGURE_BRACKET = "}";


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

    public static final List<String> OPEN_BRACKETS_LIST = Arrays.asList(
            OPEN_ROUND_BRACKET,
            OPEN_FIGURE_BRACKET,
            "[");

    public static final List<String> CLOSE_BRACKETS_LIST = Arrays.asList(
            CLOSE_ROUND_BRACKET,
            CLOSE_FIGURE_BRACKET,
            "]");


    public static final List<String> OTHER_OPERATORS_LIST = Arrays.asList(
            ";",
            ",",
            ".",
            "...",
            "@",
            "::"
    );

    public static final List<String> OPERATORS_SEPARATORS_LIST = Stream.of(
                    OPEN_BRACKETS_LIST,
                    CLOSE_BRACKETS_LIST,
                    OTHER_OPERATORS_LIST)
            .flatMap(Collection::stream)
            .toList();

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

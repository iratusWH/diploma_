package support.classes;

public class RegexPatterns {
    private RegexPatterns(){}

    public static final String CLASS_OR_INTERFACE_DECLARATION_TEMPLATE = "^[A-Z][a-zA-Z0-9]*$";
    public static final String VARIABLE_DECLARATION_TEMPLATE = "^[a-z][a-zA-Z0-9]*$";
    public static final String CONST_DECLARATION_TEMPLATE = "^[A-Z][A-Z0-9]*(_[A-Z0-9]+)*$";
}

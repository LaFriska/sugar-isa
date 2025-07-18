package xyz.haroldgao.sugarisa.parser;

public class UnknownKeywordError extends ParseError{
    protected UnknownKeywordError(String assembly, int line, String keyword) {
        super(assembly, line, "The following keyword is not known: " + "\"" + (keyword == null ? "null" : null) + "\".");
    }
}

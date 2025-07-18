package xyz.haroldgao.sugarisa.parser;

public class UnknownKeywordException extends ParseError{
    protected UnknownKeywordException(String assembly, int line, String keyword) {
        super(assembly, line, "The following keyword is not known: " + "\"" + (keyword == null ? "null" : null) + "\".");
    }
}

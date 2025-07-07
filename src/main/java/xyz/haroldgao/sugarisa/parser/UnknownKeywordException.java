package xyz.haroldgao.sugarisa.parser;

public class UnknownKeywordException extends ParseError{
    protected UnknownKeywordException(Parser p, String keyword) {
        super(p, "The following keyword is not known: " + "\"" + (keyword == null ? "null" : null) + "\".");
    }
}

package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.ErrorInfo;

public class UnknownKeywordError extends ParseError{
    protected UnknownKeywordError(ErrorInfo errorInfo, String keyword) {
        super(errorInfo, "The following keyword is not known: " + "\"" + (keyword == null ? "null" : null) + "\".");
    }
}

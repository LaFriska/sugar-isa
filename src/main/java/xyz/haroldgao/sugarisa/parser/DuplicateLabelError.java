package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.ErrorInfo;

public class DuplicateLabelError extends ParseError {

    protected DuplicateLabelError(ErrorInfo errorInfo, String label) {
        super(errorInfo, "A duplicate label has been found: " + "\"" + label + "\".");
    }
}

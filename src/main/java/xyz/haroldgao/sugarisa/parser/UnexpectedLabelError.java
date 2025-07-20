package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.ErrorInfo;

class UnexpectedLabelError extends ParseError{
    protected UnexpectedLabelError(ErrorInfo errorInfo, String label) {
        super(errorInfo, "An undefined label " + "\"" +label + "\" was found.");
    }
}

package xyz.haroldgao.sugarisa.parser;

public class DuplicateLabelException extends ParseError {

    protected DuplicateLabelException(Parser p, String label) {
        super(p, "A duplicate label has been found: " + "\"" + label + "\".");
    }
}

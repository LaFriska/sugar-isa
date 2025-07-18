package xyz.haroldgao.sugarisa.parser;

public class DuplicateLabelException extends ParseError {

    protected DuplicateLabelException(String assembly, int line, String label) {
        super(assembly, line, "A duplicate label has been found: " + "\"" + label + "\".");
    }
}

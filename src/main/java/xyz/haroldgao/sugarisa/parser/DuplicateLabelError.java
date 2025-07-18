package xyz.haroldgao.sugarisa.parser;

public class DuplicateLabelError extends ParseError {

    protected DuplicateLabelError(String assembly, int line, String label) {
        super(assembly, line, "A duplicate label has been found: " + "\"" + label + "\".");
    }
}

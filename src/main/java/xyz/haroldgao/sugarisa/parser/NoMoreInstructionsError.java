package xyz.haroldgao.sugarisa.parser;

public class NoMoreInstructionsError extends ParseError {
    public NoMoreInstructionsError(Parser p, String message) {
        super(p, message);
    }
}

package xyz.haroldgao.sugarisa.tokeniser;

/**
 * Thrown when a strings containing invalid tokens are input into the tokeniser.
 * */
public abstract class TokenException extends RuntimeException {

    public TokenException(int pchar, int pline, String message){
        super("Syntax error at " + pline + ":" + pchar + ". " + message);
    }

    public static class InvalidImmediateException extends TokenException{
        public InvalidImmediateException(int pchar, int pline, String immediate) {
            super(pchar, pline, "The following immediate value is incorrectly formatted: " + "\"" + immediate + "\".");
        }
    }

    public static class UnexpectedSymbolException extends TokenException{
        public UnexpectedSymbolException(int pchar, int pline, char symbol) {
            super(pchar, pline, "Unexpected symbol found: " + "\"" + symbol + "\".");
        }
    }

    public static class UnexpectedWordException extends TokenException{
        public UnexpectedWordException(int pchar, int pline, String word) {
            super(pchar, pline, "Unexpected word found: " + "\"" + word + "\".");
        }
    }

}
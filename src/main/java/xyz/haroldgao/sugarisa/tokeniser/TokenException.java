package xyz.haroldgao.sugarisa.tokeniser;

/**
 * Thrown when a strings containing invalid tokens are input into the tokeniser.
 * */
public abstract class TokenException extends Exception {

    public TokenException(int pchar, int pline, String message){
        super("Syntax error at " + pline + ":" + pchar + ". " + message);
    }

    public static class InvalidImmediateException extends TokenException{
        public InvalidImmediateException(int pchar, int pline, String immediate) {
            super(pchar, pline, "The following immediate value is incorrectly formatted: " + "\"" + immediate + "\".");
        }
    }

}
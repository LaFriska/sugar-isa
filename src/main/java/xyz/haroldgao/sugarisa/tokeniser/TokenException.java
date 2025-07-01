package xyz.haroldgao.sugarisa.tokeniser;

/**
 * Thrown when a strings containing invalid tokens are input into the tokeniser.
 * */
public abstract class TokenException extends RuntimeException {

    public TokenException(Tokeniser t, String message){
        super("Syntax error at " + t.pline + ":" + t.pchar + ". " + message + "\n\n" + "Assembly Code:" + "\n" + t.buffer);
    }

    public static class InvalidImmediateException extends TokenException{
        public InvalidImmediateException(Tokeniser t, String immediate) {
            super(t, "The following immediate value is incorrectly formatted: " + "\"" + immediate + "\".");
        }
    }

    public static class UnexpectedSymbolException extends TokenException{
        public UnexpectedSymbolException(Tokeniser t, char symbol) {
            super(t, "Unexpected symbol found: " + "\"" + symbol + "\".");
        }
    }

    public static class UnexpectedWordException extends TokenException{
        public UnexpectedWordException(Tokeniser t, String word) {
            super(t, "Unexpected word found: " + "\"" + word + "\".");
        }
    }

    public static class NoMoreTokensException extends TokenException{

        public NoMoreTokensException(Tokeniser t) {
            super(t, "No more tokens can be extracted from the remaining buffer.");
        }
    }

}
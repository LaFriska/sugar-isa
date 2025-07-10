package xyz.haroldgao.sugarisa.tokeniser;

/**
 * Thrown when a strings containing invalid tokens are input into the tokeniser.
 * */
public abstract class TokenError extends RuntimeException {

    public TokenError(Tokeniser t, String message){
        super("Syntax error at " + t.pline + ":" + t.pchar + ". " + message + "\n\n" + "Assembly Code:" + "\n" + t.buffer);
    }

    public static class InvalidImmediateError extends TokenError {
        public InvalidImmediateError(Tokeniser t, String immediate) {
            super(t, "The following immediate value is incorrectly formatted: " + "\"" + immediate + "\".");
        }
    }

    public static class UnexpectedSymbolError extends TokenError {
        public UnexpectedSymbolError(Tokeniser t, char symbol) {
            super(t, "Unexpected symbol found: " + "\"" + symbol + "\".");
        }
    }

    public static class UnexpectedWordError extends TokenError {
        public UnexpectedWordError(Tokeniser t, String word) {
            super(t, "Unexpected word found: " + "\"" + word + "\".");
        }
    }

    public static class NoMoreTokensError extends TokenError {

        public NoMoreTokensError(Tokeniser t) {
            super(t, "No more tokens can be extracted from the remaining buffer.");
        }
    }

}
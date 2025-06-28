package xyz.haroldgao.sugarisa.tokeniser;

/**
 * Thrown when a strings containing invalid tokens are input into the tokeniser.
 * */
public class TokenException extends Exception {

    public TokenException(String buffer, int pchar, int pline, int p){
        StringBuilder sb = new StringBuilder("Syntax error at " + pline + ":" + pchar + ".");
    }
}

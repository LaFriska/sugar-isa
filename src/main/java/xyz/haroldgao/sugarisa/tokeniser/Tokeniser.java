package xyz.haroldgao.sugarisa.tokeniser;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A tokeniser for Sugar assembly code. See the specification.md file in root directory.
 * A tokeniser takes a string input and converts it to a list of tokens. This is done sequentially
 * using an Iterator.
 * */
public class Tokeniser {

    private static final Set<Character> HEX_CHARS = new HashSet<>(List.of('a','b','c','d','e','f'));

    private final String buffer;

    private int pchar = 1; //pointer to the character in a given line of code

    private int pline = 1; //pointer to the line of code.

    private int p = 0; //pointer to the char within the buffer.

    public Tokeniser(String buffer){
        this.buffer = buffer;
        processWhitespace();
    }

    /**
     * Returns whether the buffer contains further tokens.
     * */
    public boolean hasNext() {
        return p < buffer.length();
    }

    /**
     * Returns the next token. We can assume that p is pointing at a non-whitespace character.
     * */
    public Token next() throws TokenException {

        if(!hasNext()) return null;

        //Tokenise comments
        if(buffer.startsWith("//")){
            return tokeniseComment();
        }

        //Tokens with exactly two characters.
        for (TokenType tokType : TokenType.TWO_CHAR_TOKENS) {

            assert tokType.value != null;

            if(buffer.startsWith(tokType.value)){
                updatePointer(2);
                return new Token(tokType);
            }
        }

        //Tokens with one character
        for (TokenType tokType : TokenType.SINGLE_CHAR_TOKENS) {

            assert tokType.value != null;

            if(buffer.startsWith(tokType.value)){
                updatePointer(1);
                return new Token(tokType);
            }
        }

        //Here we assume the token is alphanumerical possibly with underscores.
        //Under our whitespace rules, we tokenise whatever we can until we hit a whitespace.

        StringBuilder sb = new StringBuilder();

        while(p < buffer.length() && !breaksAlphanumericalToken(buffer.charAt(p))){
            sb.append(buffer.charAt(p));
            incrementPointers();
        }

        String word = sb.toString();

        if(word.startsWith("0x")){

            if(word.length() == 2)
                throw new TokenException.InvalidImmediateException(pchar, pline, "");

            String hex = word.substring(2);
            if(!isValidHex(hex)) throw new TokenException.InvalidImmediateException(pchar, pline, hex);

            return new Token(TokenType.IMM_HEX, hex);

        }
        return null;
    }

    /**
     * Pre-Condition: p points to the first backslash of the comment,
     * tokenises the comment and returns the token, while updating all three
     * pointers appropriately.
     * */
    private Token tokeniseComment(){

        p += 2;
        pchar += 2;
        StringBuilder sb = new StringBuilder();

        while(p < buffer.length() && buffer.charAt(p) != '\n'){
            sb.append(buffer.charAt(p));
            p++;
            pchar++;
        }
        processWhitespace();
        return new Token(TokenType.COMMENT, sb.toString());
    }


    private void updatePointer(int inc){
        p += inc;
        pchar += inc;
        processWhitespace();
    }

    protected static boolean isValidHex(@NotNull String str){
        if(str.isEmpty()) return false;
        for (int i = 0; i < str.length(); i++) {
            if(!HEX_CHARS.contains(str.charAt(i)) && !Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * Sets the pointer to the first character that is not a whitespace.
     * */
    protected void processWhitespace(){
        while(p < buffer.length() && isWhitespace(buffer.charAt(p))){

            incrementPointers();
        }
    }

    private void incrementPointers() {
        if(buffer.charAt(p) == '\n'){
            pline++;
            pchar = 0;
        }else{
            pchar++;
        }
        p++;
    }

    /**
     * Returns whether the given character is a whitespace.
     * */
    protected static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f';
    }

    protected static boolean breaksAlphanumericalToken(char c){
        return !Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_';
    }
}

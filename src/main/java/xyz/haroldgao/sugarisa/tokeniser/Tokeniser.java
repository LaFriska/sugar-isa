package xyz.haroldgao.sugarisa.tokeniser;

import java.util.Iterator;

/**
 * A tokeniser for Sugar assembly code. See the specification.md file in root directory.
 * A tokeniser takes a string input and converts it to a list of tokens. This is done sequentially
 * using an Iterator.
 * */
public class Tokeniser implements Iterator<Token> {

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
    @Override
    public boolean hasNext() {
        return p < buffer.length();
    }

    /**
     * Returns the next token. We can assume that p is pointing at a non-whitespace character.
     * */
    @Override
    public Token next() {

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

        while(p < buffer.length() && !isWhitespace(buffer.charAt(p))){
            sb.append(buffer.charAt(p));
            incrementPointers();
        }

        //todo


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
}

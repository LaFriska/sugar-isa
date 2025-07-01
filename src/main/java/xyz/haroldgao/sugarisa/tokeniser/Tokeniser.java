package xyz.haroldgao.sugarisa.tokeniser;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

/**
 * A tokeniser for Sugar assembly code. See the specification.md file in root directory.
 * A tokeniser takes a string input and converts it to a list of tokens. This is done sequentially
 * using an Iterator.
 */
public class Tokeniser implements Iterator<Token> {

    protected final String buffer;

    protected int pchar = 1; //pointer to the character in a given line of code

    protected int pline = 1; //pointer to the line of code.

    protected int p = 0; //pointer to the char within the buffer.

    public Tokeniser(String buffer) {
        this.buffer = buffer;
        processWhitespace();
    }

    /**
     * Returns whether the buffer contains further tokens.
     */
    @Override
    public boolean hasNext() {
        return p < buffer.length();
    }

    /**
     * Returns the next token. We can assume that p is pointing at a non-whitespace character.
     *
     * @throws TokenException.InvalidImmediateException if an immediate value is incorrectly formatted.
     * @throws TokenException.UnexpectedSymbolException if an unexpected (untokenisable) symbol has been found in the buffer.
     * @throws TokenException.UnexpectedWordException if an unexpected word has been found (a word that is not an immediate, keyword, or label).
     * @throws TokenException.NoMoreTokensException if no further tokens may be extracted from the buffer.
     */
    @Override
    public @NotNull Token next() {

        if (!hasNext()) throw new TokenException.NoMoreTokensException(this);

        //Tokenise comments
        if (nextTwoChars("//")) {
            return tokeniseComment();
        }

        //Tokens with exactly two characters.
        for (TokenType tokType : TokenType.TWO_CHAR_TOKENS) {

            assert tokType.value != null;

            if (nextTwoChars(tokType.value)) {
                updatePointer(2);
                return new Token(tokType);
            }
        }

        //Tokens with one character
        for (TokenType tokType : TokenType.SINGLE_CHAR_TOKENS) {

            assert tokType.value != null;

            if (String.valueOf(buffer.charAt(p)).equals(tokType.value)) {
                updatePointer(1);
                return new Token(tokType);
            }
        }

        if(TokeniserUtils.breaksAlphanumericalToken(buffer.charAt(p))){
            throw new TokenException.UnexpectedSymbolException(this, buffer.charAt(p));
        }

        //Here we assume the token is alphanumerical possibly with underscores.
        //Under our whitespace rules, we tokenise whatever we can until we hit a whitespace.

        StringBuilder sb = new StringBuilder();

        while (p < buffer.length() && !TokeniserUtils.breaksAlphanumericalToken(buffer.charAt(p))) {
            sb.append(buffer.charAt(p));
            incrementPointers();
        }

        String word = sb.toString();

        //Hex immediates
        if (word.startsWith("0x")) {
            processWhitespace();
            return tokeniseImmediate(word, TokenType.IMM_HEX, TokeniserUtils.IS_HEX_DIGIT);
        }

        //Binary immediates
        if (word.startsWith("0b")) {
            processWhitespace();
            return tokeniseImmediate(word, TokenType.IMM_BIN, TokeniserUtils.IS_BINARY_DIGIT);
        }

        //Decimals
        if (isDecimal(word)) {
            processWhitespace();
            return new Token(TokenType.IMM_DEC, word);
        }

        //Labels
        if (isLabel(word)) {
            processWhitespace();
            return new Token(TokenType.LABEL, word);
        }

        //Keywords
        if (isKeyword(word)) {
            processWhitespace();
            return new Token(TokenType.KEYWORD, word);
        }

        throw new TokenException.UnexpectedWordException(this, word);
    }

    /**
     * Returns whether the two characters from the pointer p are exactly that of a given
     * string.
     * */
    private boolean nextTwoChars(@NotNull String tokenValue) {
        return buffer.charAt(p) == tokenValue.charAt(0) &&
               (p + 1 < buffer.length() && buffer.charAt(p + 1) == tokenValue.charAt(1));
    }

    public List<Token> getAllTokens(){
        ArrayList<Token> res = new ArrayList<>();
        while(hasNext()){
            res.add(next());
        }
        return res;
    }

    /**
     * Checks if a word is a decimal.
     */
    private boolean isDecimal(String word) {
        return TokeniserUtils.isValid(word, Character::isDigit);
    }

    /**
     * Checks if a word is a keyword.
     */
    private boolean isKeyword(@NotNull String word) {
        Predicate<Character> pred = (c) ->
                Character.isDigit(c) || //is a digit
                        (Character.isAlphabetic(c) && Character.toLowerCase(c) == c); //or lower case alphabet
        return TokeniserUtils.isValid(word, pred);
    }

    /**
     * Checks if a word is a label.
     */
    private boolean isLabel(@NotNull String word) {
        Predicate<Character> pred = (c) ->
                Character.isDigit(c) || //is a digit
                        (Character.isAlphabetic(c) && Character.toUpperCase(c) == c) || //or upper case alphabet
                        (c == '_');
        return TokeniserUtils.isValid(word, pred);
    }

    /**
     * Tokenises an immediate, given a hex immediate given a character-wise predicate and a string.
     *
     * @param pred a predicate where each character in the string must satisfy for validity to hold.
     * @param type the {@link TokenType} of the resulting tokenisation.
     * @throws TokenException.InvalidImmediateException if the word contains invalid (non-hexadecimal) characters
     *                                                  according to the predicate.
     */
    private @NotNull Token tokeniseImmediate(String word, TokenType type, Predicate<Character> pred) {
        if (word.length() == 2)
            throw new TokenException.InvalidImmediateException(this, "");

        String hex = word.substring(2);
        if (!TokeniserUtils.isValid(hex, pred))
            throw new TokenException.InvalidImmediateException(this, hex);

        return new Token(type, hex);
    }

    /**
     * Pre-Condition: p points to the first backslash of the comment,
     * tokenises the comment and returns the token, while updating all three
     * pointers appropriately.
     */
    private Token tokeniseComment() {

        p += 2;
        pchar += 2;
        StringBuilder sb = new StringBuilder();

        while (p < buffer.length() && buffer.charAt(p) != '\n') {
            sb.append(buffer.charAt(p));
            p++;
            pchar++;
        }
        processWhitespace();
        return new Token(TokenType.COMMENT, sb.toString());
    }


    private void updatePointer(int inc) {
        p += inc;
        pchar += inc;
        processWhitespace();
    }

    /**
     * Sets the pointer to the first character that is not a whitespace.
     */
    protected void processWhitespace() {
        while (p < buffer.length() && TokeniserUtils.isWhitespace(buffer.charAt(p))) {
            incrementPointers();
        }
    }

    private void incrementPointers() {
        if (buffer.charAt(p) == '\n') {
            pline++;
            pchar = 0;
        } else {
            pchar++;
        }
        p++;
    }
}

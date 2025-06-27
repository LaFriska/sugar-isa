package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for various utility methods in {@link Tokeniser}.
 * */
public class TokeniserUtilTest {

    @Test
    public void testIsWhiteSpace(){
        assertTrue(Tokeniser.isWhitespace('\n'));
        assertTrue(Tokeniser.isWhitespace(' '));
        assertTrue(Tokeniser.isWhitespace('\r'));
        assertTrue(Tokeniser.isWhitespace('\f'));
        assertTrue(Tokeniser.isWhitespace('\t'));

        assertFalse(Tokeniser.isWhitespace('a'));
        assertFalse(Tokeniser.isWhitespace(';'));
        assertFalse(Tokeniser.isWhitespace(':'));
        assertFalse(Tokeniser.isWhitespace('*'));
        assertFalse(Tokeniser.isWhitespace('%'));
    }

    @Test
    public void testTokenTypeSegregation(){
        TokenType[] singleChar = TokenType.SINGLE_CHAR_TOKENS;
        TokenType[] twoChar = TokenType.TWO_CHAR_TOKENS;

        for (TokenType tokenType : singleChar) {
            assertNotNull(tokenType.value);
            assertEquals(1, tokenType.value.length());
        }

        for (TokenType tokenType : twoChar) {
            assertNotNull(tokenType.value);
            assertEquals(2, tokenType.value.length());
        }

    }

}

package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test cases for various utility methods in {@link Tokeniser}.
 * */
public class TokeniserUtilTest {

    @Test
    public void testIsWhiteSpace(){
        assertTrue(TokeniserUtils.isWhitespace('\n'));
        assertTrue(TokeniserUtils.isWhitespace(' '));
        assertTrue(TokeniserUtils.isWhitespace('\r'));
        assertTrue(TokeniserUtils.isWhitespace('\f'));
        assertTrue(TokeniserUtils.isWhitespace('\t'));

        assertFalse(TokeniserUtils.isWhitespace('a'));
        assertFalse(TokeniserUtils.isWhitespace(';'));
        assertFalse(TokeniserUtils.isWhitespace(':'));
        assertFalse(TokeniserUtils.isWhitespace('*'));
        assertFalse(TokeniserUtils.isWhitespace('%'));
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

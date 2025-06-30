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

        for (TokenType tokenType : TokenType.SINGLE_CHAR_TOKENS) {
            assertNotNull(tokenType.value);
            assertEquals(1, tokenType.value.length());
        }

        for (TokenType tokenType : TokenType.TWO_CHAR_TOKENS) {
            assertNotNull(tokenType.value);
            assertEquals(2, tokenType.value.length());
        }

    }

    @Test
    public void testTokenToString(){
        assertEquals("{COMMENT, This is a comment.}", new Token(TokenType.COMMENT, "This is a comment.").toString());
        assertEquals("{LABEL, This is a label.}", new Token(TokenType.LABEL, "This is a label.").toString());
        assertEquals("{;}", new Token(TokenType.TERM, "This is a comment.").toString());
        assertEquals("{+=}", new Token(TokenType.ADD_EQ, "Add eq.").toString());
    }

}

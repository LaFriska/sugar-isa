package xyz.haroldgao.sugarisa.parser;

import org.junit.Test;
import xyz.haroldgao.sugarisa.tokeniser.Tokeniser;

import static org.junit.Assert.*;

/**
 * Tests for various parser util methods.
 * */
public class ParserUtilTest {

    @Test
    public void testParseBinaryImmediates() {
        Parser parser = new Parser(new Tokeniser(""));
        assertEquals(3, parser.parseImmediate("00011", 4, 2));
        assertEquals(3, parser.parseImmediate("00011", 3, 2));
        assertEquals(0, parser.parseImmediate("0", 1, 2));
        assertEquals(1, parser.parseImmediate("1", 1, 2));
    }

    @Test
    public void testParseDecimalImmediates() {
        Parser parser = new Parser(new Tokeniser(""));
        assertEquals(15, parser.parseImmediate("15", 4, 10));
        assertEquals(7, parser.parseImmediate("7", 3, 10));
        assertEquals(0, parser.parseImmediate("0", 8, 10));
    }

    @Test
    public void testParseHexImmediates() {
        Parser parser = new Parser(new Tokeniser(""));
        assertEquals(0xF, parser.parseImmediate("F", 4, 16));
        assertEquals(0x1A, parser.parseImmediate("1A", 8, 16));
        assertEquals(0x0, parser.parseImmediate("0", 1, 16));
    }

    @Test(expected = OversizedImmediateError.class)
    public void testOversizedImmediateBinary() {
        Parser parser = new Parser(new Tokeniser(""));
        parser.parseImmediate("10000", 4, 2); // 5-bit value in 4 bits
    }

    @Test(expected = OversizedImmediateError.class)
    public void testOversizedImmediateDecimal() {
        Parser parser = new Parser(new Tokeniser(""));
        parser.parseImmediate("256", 8, 10); // 256 needs 9 bits
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidImmediateInput() {
        Parser parser = new Parser(new Tokeniser(""));
        parser.parseImmediate("XYZ", 8, 16); // invalid hex
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeImmediateInput() {
        Parser parser = new Parser(new Tokeniser(""));
        parser.parseImmediate("-1", 8, 10); // parseUnsignedInt should reject this
    }

    @Test
    public void testSignExtension() {
        Parser parser = new Parser(new Tokeniser(""));

        // 5-bit signed values
        assertEquals(15, parser.signExtend(0b01111, 5));  // +15
        assertEquals(-1, parser.signExtend(0b11111, 5));  // -1
        assertEquals(-8, parser.signExtend(0b11000, 5));  // -8
        assertEquals(0, parser.signExtend(0b00000, 5));   // 0

        // 6-bit signed values
        assertEquals(31, parser.signExtend(0b011111, 6));   // +31
        assertEquals(-1, parser.signExtend(0b111111, 6));   // -1
        assertEquals(-16, parser.signExtend(0b110000, 6));  // -16

        // 8-bit signed values
        assertEquals(127, parser.signExtend(0b01111111, 8));  // +127
        assertEquals(-1, parser.signExtend(0b11111111, 8));   // -1
        assertEquals(-128, parser.signExtend(0b10000000, 8)); // -128

        // Already 32-bit value
        assertEquals(-123456789, parser.signExtend(-123456789, 32));
        assertEquals(123456789, parser.signExtend(123456789, 32));
    }


}

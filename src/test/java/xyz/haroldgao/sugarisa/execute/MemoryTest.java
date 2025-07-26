package xyz.haroldgao.sugarisa.execute;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * These test cases were generated using ChatGPT.
 * */
public class MemoryTest {

    @Test
    public void testReadWriteSingleByte() {
        Memory mem = new Memory();
        int addr = 0x1000;

        // Initially zero
        assertEquals(0, mem.read(addr));

        // Write and read back
        mem.write(addr, (byte) 0x7F);
        assertEquals((byte) 0x7F, mem.read(addr));

        // Overwrite
        mem.write(addr, (byte) 0xFF);
        assertEquals((byte) 0xFF, mem.read(addr));
    }

    @Test
    public void testDefaultUninitializedIsZero() {
        Memory mem = new Memory();
        assertEquals(0, mem.read(0));
        assertEquals(0, mem.read(0xABCDEF));
    }

    @Test
    public void testReadWriteWordLittleEndian() {
        Memory mem = new Memory();
        int addr = 0x2000;
        int word = 0x12345678;

        mem.writeWord(addr, word);
        assertEquals(word, mem.readWord(addr));

        // Verify each byte in memory
        assertEquals((byte) 0x78, mem.read(addr));
        assertEquals((byte) 0x56, mem.read(addr + 1));
        assertEquals((byte) 0x34, mem.read(addr + 2));
        assertEquals((byte) 0x12, mem.read(addr + 3));
    }

    @Test
    public void testPartialByteUpdate() {
        Memory mem = new Memory();
        int addr = 0x3000;
        mem.writeWord(addr, 0xAABBCCDD);

        // Change only the second byte
        mem.write(addr + 1, (byte) 0x99);

        int expected = 0xAABB99DD;
        // Little-endian layout: DD CC BB AA
        // After update: DD 99 BB AA → 0xAABB99DD
        assertEquals(expected, mem.readWord(addr));
    }

    @Test
    public void testWrapAroundOnWordRead() {
        Memory mem = new Memory();

        // Write last byte of memory and first 3 bytes
        mem.write(0xFFFFFFFF, (byte) 0x11);
        mem.write(0x00000000, (byte) 0x22);
        mem.write(0x00000001, (byte) 0x33);
        mem.write(0x00000002, (byte) 0x44);

        int value = mem.readWord(0xFFFFFFFF);
        // Little-endian: [0x11, 0x22, 0x33, 0x44] → 0x44332211
        assertEquals(0x44332211, value);
    }

    @Test
    public void testOverwriteWord() {
        Memory mem = new Memory();
        int addr = 0x4000;

        mem.writeWord(addr, 0x11223344);
        mem.writeWord(addr, 0xAABBCCDD);

        assertEquals(0xAABBCCDD, mem.readWord(addr));
    }

    @Test
    public void testMaxAddressByteAccess() {
        Memory mem = new Memory();

        // Last address
        mem.write(0xFFFFFFFF, (byte) 0x7A);
        assertEquals((byte) 0x7A, mem.read(0xFFFFFFFF));

        // Check wrap-around works for increment logic (optional)
    }

    @Test
    public void stressTestSparseMemory() {
        Memory mem = new Memory();

        // Allocate sparse regions
        mem.write(0x00000000, (byte) 0x01);
        mem.write(0x7FFFFFFF, (byte) 0x02);
        mem.write(0xFFFFFFFF, (byte) 0x03);

        assertEquals((byte) 0x01, mem.read(0x00000000));
        assertEquals((byte) 0x02, mem.read(0x7FFFFFFF));
        assertEquals((byte) 0x03, mem.read(0xFFFFFFFF));
    }
}

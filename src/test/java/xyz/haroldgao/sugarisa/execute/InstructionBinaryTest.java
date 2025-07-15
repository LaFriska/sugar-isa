package xyz.haroldgao.sugarisa.execute;

import org.junit.Assert;
import org.junit.Test;
import xyz.haroldgao.sugarisa.execute.instructions.*;

/**
 * This class tests {@link Instruction#getBinary()}
 * */

public class InstructionBinaryTest {

    @Test
    public void testSet(){
        test(new SetInstruction(Register.R0, Register.R0), 0);
        test(new SetInstruction(Register.LR, Register.SP), 0b00000011010000000000000000001111);
        test(new SetInstruction(Register.LR, 0b1111001110011010011010), 0b00000111011111001110011010011010);
    }

    @Test
    public void testNot(){
        test(new NotInstruction(Register.R0, Register.R0), 0b01001 << 27);
        test(new NotInstruction(Register.R9, Register.SP), 0b01001010010000000000000000001111);
        test(new NotInstruction(Register.LR, 0b1111001110011010011010), 0b01001111011111001110011010011010);
    }

    @Test
    public void testAdd(){
        test(new AddInstruction(Register.PC, Register.R6, Register.FL, false), 0b00001011100011000000000000001100);
        test(new AddInstruction(Register.PC, Register.R6, Register.FL, true), 0b00001011101011000000000000001100);
        test(new AddInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b00001111100011001111000011110000);
        test(new AddInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b00001111101011001111000011110000);
    }

    @Test
    public void testSub(){
        test(new SubInstruction(Register.PC, Register.R6, Register.FL, false), 0b00010011100011000000000000001100);
        test(new SubInstruction(Register.PC, Register.R6, Register.FL, true), 0b00010011101011000000000000001100);
        test(new SubInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b00010111100011001111000011110000);
        test(new SubInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b00010111101011001111000011110000);
    }

    @Test
    public void testMul(){
        test(new MulInstruction(Register.PC, Register.R6, Register.FL, false), 0b00011011100011000000000000001100);
        test(new MulInstruction(Register.PC, Register.R6, Register.FL, true), 0b00011011101011000000000000001100);
        test(new MulInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b00011111100011001111000011110000);
        test(new MulInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b00011111101011001111000011110000);
    }

    @Test
    public void testDiv(){
        test(new DivInstruction(Register.PC, Register.R6, Register.FL, false), 0b00100011100011000000000000001100);
        test(new DivInstruction(Register.PC, Register.R6, Register.FL, true), 0b00100011101011000000000000001100);
        test(new DivInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b00100111100011001111000011110000);
        test(new DivInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b00100111101011001111000011110000);
    }

    @Test
    public void testMod(){
        test(new ModInstruction(Register.PC, Register.R6, Register.FL, false), 0b00101011100011000000000000001100);
        test(new ModInstruction(Register.PC, Register.R6, Register.FL, true), 0b00101011101011000000000000001100);
        test(new ModInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b00101111100011001111000011110000);
        test(new ModInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b00101111101011001111000011110000);
    }

    @Test
    public void testAnd(){
        test(new AndInstruction(Register.PC, Register.R6, Register.FL, false), 0b00110011100011000000000000001100);
        test(new AndInstruction(Register.PC, Register.R6, Register.FL, true), 0b00110011101011000000000000001100);
        test(new AndInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b00110111100011001111000011110000);
        test(new AndInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b00110111101011001111000011110000);
    }

    @Test
    public void testOr(){
        test(new OrInstruction(Register.PC, Register.R6, Register.FL, false), 0b00111011100011000000000000001100);
        test(new OrInstruction(Register.PC, Register.R6, Register.FL, true), 0b00111011101011000000000000001100);
        test(new OrInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b00111111100011001111000011110000);
        test(new OrInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b00111111101011001111000011110000);
    }

    @Test
    public void testXor(){
        test(new XorInstruction(Register.PC, Register.R6, Register.FL, false), 0b01000011100011000000000000001100);
        test(new XorInstruction(Register.PC, Register.R6, Register.FL, true), 0b01000011101011000000000000001100);
        test(new XorInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b01000111100011001111000011110000);
        test(new XorInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b01000111101011001111000011110000);
    }

    @Test
    public void testLeftShift(){
        test(new LeftShiftInstruction(Register.PC, Register.R6, Register.FL, false), 0b01010011100011000000000000001100);
        test(new LeftShiftInstruction(Register.PC, Register.R6, Register.FL, true), 0b01010011101011000000000000001100);
        test(new LeftShiftInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b01010111100011001111000011110000);
        test(new LeftShiftInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b01010111101011001111000011110000);
    }

    @Test
    public void testRightShift(){
        test(new RightShiftInstruction(Register.PC, Register.R6, Register.FL, false), 0b01011011100011000000000000001100);
        test(new RightShiftInstruction(Register.PC, Register.R6, Register.FL, true), 0b01011011101011000000000000001100);
        test(new RightShiftInstruction(Register.PC, Register.R6, 0b1111000011110000, false), 0b01011111100011001111000011110000);
        test(new RightShiftInstruction(Register.PC, Register.R6, 0b1111000011110000, true), 0b01011111101011001111000011110000);
    }

    @Test
    public void standardReadTest(){
        test(new MemoryReadInstruction(Register.PC, Register.R6, Register.FL, false, OffsetType.STANDARD), 0b01100011100011000000000000001100);
        test(new MemoryReadInstruction(Register.PC, Register.R6, Register.FL, true, OffsetType.STANDARD), 0b01100011101011000000000000001100);
        test(new MemoryReadInstruction(Register.PC, Register.R6, 0b1111000011110000, false, OffsetType.STANDARD), 0b01100111100011001111000011110000);
        test(new MemoryReadInstruction(Register.PC, Register.R6, 0b1111000011110000, true, OffsetType.STANDARD), 0b01100111101011001111000011110000);
    }

    @Test
    public void standardWriteTest(){
        test(new MemoryWriteInstruction(Register.PC, Register.R6, Register.FL, false, OffsetType.STANDARD), 0b01101011100011000000000000001100);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, Register.FL, true, OffsetType.STANDARD), 0b01101011101011000000000000001100);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, 0b1111000011110000, false, OffsetType.STANDARD), 0b01101111100011001111000011110000);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, 0b1111000011110000, true, OffsetType.STANDARD), 0b01101111101011001111000011110000);
    }

    @Test
    public void postReadTest(){
        test(new MemoryReadInstruction(Register.PC, Register.R6, Register.FL, false, OffsetType.POST), 0b01110011100011010000000000001100);
        test(new MemoryReadInstruction(Register.PC, Register.R6, Register.FL, true, OffsetType.POST), 0b01110011101011010000000000001100);
        test(new MemoryReadInstruction(Register.PC, Register.R6, 0b1111000011110000, false, OffsetType.POST), 0b01110111100011011111000011110000);
        test(new MemoryReadInstruction(Register.PC, Register.R6, 0b1111000011110000, true, OffsetType.POST), 0b01110111101011011111000011110000);
    }

    @Test
    public void preReadTest(){
        test(new MemoryReadInstruction(Register.PC, Register.R6, Register.FL, false, OffsetType.PRE), 0b01110011100011000000000000001100);
        test(new MemoryReadInstruction(Register.PC, Register.R6, Register.FL, true, OffsetType.PRE), 0b01110011101011000000000000001100);
        test(new MemoryReadInstruction(Register.PC, Register.R6, 0b1111000011110000, false, OffsetType.PRE), 0b01110111100011001111000011110000);
        test(new MemoryReadInstruction(Register.PC, Register.R6, 0b1111000011110000, true, OffsetType.PRE), 0b01110111101011001111000011110000);
    }

    @Test
    public void postWriteTest(){
        test(new MemoryWriteInstruction(Register.PC, Register.R6, Register.FL, false, OffsetType.POST), 0b01111011100011010000000000001100);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, Register.FL, true, OffsetType.POST), 0b01111011101011010000000000001100);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, 0b1111000011110000, false, OffsetType.POST), 0b01111111100011011111000011110000);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, 0b1111000011110000, true, OffsetType.POST), 0b01111111101011011111000011110000);
    }

    @Test
    public void preWriteTest(){
        test(new MemoryWriteInstruction(Register.PC, Register.R6, Register.FL, false, OffsetType.PRE), 0b01111011100011000000000000001100);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, Register.FL, true, OffsetType.PRE), 0b01111011101011000000000000001100);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, 0b1111000011110000, false, OffsetType.PRE), 0b01111111100011001111000011110000);
        test(new MemoryWriteInstruction(Register.PC, Register.R6, 0b1111000011110000, true, OffsetType.PRE), 0b01111111101011001111000011110000);
    }

    @Test
    public void testSimpleInstructions(){
        test(new PushInstruction(0b10100101010101011001011010), 0b10000110100101010101011001011010);
        test(new PushInstruction(Register.R1), 0b10000000000000000000000000000001);
        test(new PopInstruction(0b10100101010101011001011010), 0b10001110100101010101011001011010);
        test(new PopInstruction(Register.R2), 0b10001000000000000000000000000010);

        test(new GotoInstruction(0b10100101010101011001011011), 0b10010110100101010101011001011011);
        test(new GotoInstruction(Register.R3), 0b10010000000000000000000000000011);
        test(new GotoInstruction(0b10100101010101011001011111, ALUFlag.N), 0b10011110100101010101011001011111);
        test(new GotoInstruction(Register.R4, ALUFlag.N), 0b10011000000000000000000000000100);
        test(new GotoInstruction(0b10100101010101011001011111, ALUFlag.Z), 0b10100110100101010101011001011111);
        test(new GotoInstruction(Register.R4, ALUFlag.Z), 0b10100000000000000000000000000100);
        test(new GotoInstruction(0b10100101010101011001011111, ALUFlag.C), 0b10101110100101010101011001011111);
        test(new GotoInstruction(Register.R5, ALUFlag.C), 0b10101000000000000000000000000101);
        test(new GotoInstruction(0b10100101010101011001011111, ALUFlag.V), 0b10110110100101010101011001011111);
        test(new GotoInstruction(Register.R6, ALUFlag.V), 0b10110000000000000000000000000110);

        test(new CallInstruction(0b10100101010101011001011111), 0b10111110100101010101011001011111);
        test(new CallInstruction(Register.R7), 0b10111000000000000000000000000111);
        test(new CallInstruction(0b10100101010101011001011111, ALUFlag.N), 0b11000110100101010101011001011111);
        test(new CallInstruction(Register.R8, ALUFlag.N), 0b11000000000000000000000000001000);
        test(new CallInstruction(0b10100101010101011001111111, ALUFlag.Z), 0b11001110100101010101011001111111);
        test(new CallInstruction(Register.R9, ALUFlag.Z), 0b11001000000000000000000000001001);
        test(new CallInstruction(0b10100101010101011001111111, ALUFlag.C), 0b11010110100101010101011001111111);
        test(new CallInstruction(Register.R10, ALUFlag.C), 0b11010000000000000000000000001010);
        test(new CallInstruction(0b10100101010101011001111110, ALUFlag.V), 0b11011110100101010101011001111110);
        test(new CallInstruction(Register.R11, ALUFlag.V), 0b11011000000000000000000000001011);
    }



    private void test(Instruction i, int expectedBinary){
        Assert.assertEquals(expectedBinary, i.getBinary());
    }

}

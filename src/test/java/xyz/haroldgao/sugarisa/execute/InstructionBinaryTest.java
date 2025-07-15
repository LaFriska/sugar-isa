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

    private void test(Instruction i, int expectedBinary){
        Assert.assertEquals(expectedBinary, i.getBinary());
    }

}

package xyz.haroldgao.sugarisa.execute;

import org.junit.Assert;
import org.junit.Test;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.execute.instructions.NotInstruction;
import xyz.haroldgao.sugarisa.execute.instructions.SetInstruction;

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

    private void test(Instruction i, int expectedBinary){
        Assert.assertEquals(expectedBinary, i.getBinary());
    }

}

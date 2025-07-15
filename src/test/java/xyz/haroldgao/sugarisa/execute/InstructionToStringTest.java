package xyz.haroldgao.sugarisa.execute;

import org.junit.Assert;
import org.junit.Test;
import xyz.haroldgao.sugarisa.execute.instructions.AddInstruction;
import xyz.haroldgao.sugarisa.execute.instructions.SetInstruction;

/**
 * A very simple and non-exhaustive test for instruction to string conversion.
 * */
public class InstructionToStringTest {

    @Test
    public void testSimple(){
        Assert.assertEquals("{+ r1, r2, r3}", new AddInstruction(Register.R1, Register.R2, Register.R3, false).toString());
        Assert.assertEquals("f{+ r1, r2, r3}", new AddInstruction(Register.R1, Register.R2, Register.R3, true).toString());
        Assert.assertEquals("{set sp, lr}", new SetInstruction(Register.SP, Register.LR).toString());
    }

}

package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.Register;

/**
 * Concrete implementation of {@link SoloDPInstruction} representing a SET instruction.
 * */
public final class SetInstruction extends SoloDPInstruction {

    public SetInstruction(@NotNull Register rd, @NotNull Register ra) {
        super(rd, ra);
    }

    public SetInstruction(@NotNull Register rd, int imm16) {
        super(rd, imm16);
    }

    @Override
    protected int operate(int raValueOrImm16) {
        return raValueOrImm16; //Set does not modify the data.
    }
}

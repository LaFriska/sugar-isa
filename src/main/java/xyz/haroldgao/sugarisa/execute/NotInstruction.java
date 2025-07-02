package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.Register;

/**
 * Concrete implementation of {@link SoloDPInstruction} representing a NOT instruction.
 * */
public final class NotInstruction extends SoloDPInstruction {

    public NotInstruction(@NotNull Register rd, @NotNull Register ra) {
        super(rd, ra);
    }

    public NotInstruction(@NotNull Register rd, int imm16) {
        super(rd, imm16);
    }

    @Override
    protected int operate(int raValueOrImm16) {
        return ~raValueOrImm16; //Set does not modify the data.
    }

}

package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.Register;

/**
 * Concrete implementation of {@link DuoDPInstruction} representing a SUB instruction.
 * */
public final class SubInstruction extends DuoDPInstruction {

    public SubInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb) {
        super(rd, ra, rb);
    }

    public SubInstruction(@NotNull Register rd, @NotNull Register ra, int imm16) {
        super(rd, ra, imm16);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue - rbValueOrImm16;
    }

}

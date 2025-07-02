package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.Register;

/**
 * Concrete implementation of {@link DuoDPInstruction} representing a right shift instruction.
 * */
public final class RightShiftInstruction extends DuoDPInstruction {

    public RightShiftInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb) {
        super(rd, ra, rb);
    }

    public RightShiftInstruction(@NotNull Register rd, @NotNull Register ra, int imm16) {
        super(rd, ra, imm16);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue >> rbValueOrImm16;
    }

}

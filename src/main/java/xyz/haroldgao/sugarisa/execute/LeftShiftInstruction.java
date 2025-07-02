package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;

/**
 * Concrete implementation of {@link DuoDataInstruction} representing a left shift instruction.
 * */
public final class LeftShiftInstruction extends DuoDataInstruction {

    public LeftShiftInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb, boolean setFlag) {
        super(rd, ra, rb, setFlag);
    }

    public LeftShiftInstruction(@NotNull Register rd, @NotNull Register ra, int imm16, boolean setFlag) {
        super(rd, ra, imm16, setFlag);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue << rbValueOrImm16;
    }

}

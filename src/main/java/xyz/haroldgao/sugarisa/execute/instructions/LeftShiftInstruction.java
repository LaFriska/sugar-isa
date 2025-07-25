package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Concrete implementation of {@link DuoDataInstruction} representing a left shift instruction.
 * */
public final class LeftShiftInstruction extends DuoDataInstruction {

    public LeftShiftInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb, @NotNull Boolean setFlag) {
        super(rd, ra, rb, setFlag);
    }

    public LeftShiftInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Integer imm16, @NotNull Boolean setFlag) {
        super(rd, ra, imm16, setFlag);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue << rbValueOrImm16;
    }

    @Override
    public int opcode() {
        return 0b01010000000000000000000000000000;
    }

    @Override
    protected String getMnemonic() {
        return "<<";
    }

}

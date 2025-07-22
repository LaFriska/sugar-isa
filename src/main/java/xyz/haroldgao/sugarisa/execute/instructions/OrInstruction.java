package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Concrete implementation of {@link DuoDataInstruction} representing an OR instruction.
 * */
public final class OrInstruction extends DuoDataInstruction {

    public OrInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb, @NotNull Boolean setFlag) {
        super(rd, ra, rb, setFlag);
    }

    public OrInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Integer imm16, @NotNull Boolean setFlag) {
        super(rd, ra, imm16, setFlag);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue | rbValueOrImm16;
    }

    @Override
    public int opcode() {
        return 0b00111000000000000000000000000000;
    }

    @Override
    protected String getMnemonic() {
        return "|";
    }

}

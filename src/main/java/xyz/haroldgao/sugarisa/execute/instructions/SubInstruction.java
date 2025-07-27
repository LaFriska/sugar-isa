package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Concrete implementation of {@link DuoDataInstruction} representing a SUB instruction.
 * */
public final class SubInstruction extends DuoDataInstruction {

    public SubInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb, @NotNull Boolean setFlag) {
        super(rd, ra, rb, setFlag);
    }

    public SubInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Integer imm16, @NotNull Boolean setFlag) {
        super(rd, ra, imm16, setFlag);
    }

    @Override
    protected void setFlag(int input1, int input2, int result, ArchitecturalState state) {
        int flagN = result < 0 ? 1 : 0;
        int flagZ = result == 0 ? 0b10 : 0;
        int flagC = Integer.compareUnsigned(input1, input2) < 0 ? 0 : 0b100;
        int flagV = ((input1 ^ input2) & (input1 ^ result)) < 0 ? 0b1000 : 0;
        state.flag(flagN | flagZ | flagC | flagV);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue - rbValueOrImm16;
    }

    @Override
    public int opcode() {
        return 0b00010000000000000000000000000000;
    }

    @Override
    protected String getMnemonic() {
        return "-";
    }

}

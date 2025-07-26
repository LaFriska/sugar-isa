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
    protected int operateAndSetFlag(ArchitecturalState state){
        int input1 = state.read(ra);
        int input2 = format == Format.I ? imm : state.read(rb);
        int result = operate(input1, input2);
        if(setFlag) state.flag(input1, input2, result, isArithmeticOperation, ArchitecturalState.CarryFlagMode.SUB);
        return result;
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

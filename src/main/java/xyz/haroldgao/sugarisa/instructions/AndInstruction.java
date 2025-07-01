package xyz.haroldgao.sugarisa.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.Register;

/**
 * Concrete implementation of {@link ALUInstruction} representing an AND instruction.
 * */
public final class AndInstruction extends ALUInstruction {

    public AndInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb) {
        super(rd, ra, rb);
    }

    public AndInstruction(@NotNull Register rd, @NotNull Register ra, int imm16) {
        super(rd, ra, imm16);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue & rbValueOrImm16;
    }

}

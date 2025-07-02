package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.Register;

/**
 * Concrete implementation of {@link DuoDPInstruction} representing an XOR instruction.
 * */
public final class XorInstruction extends DuoDPInstruction {

    public XorInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb) {
        super(rd, ra, rb);
    }

    public XorInstruction(@NotNull Register rd, @NotNull Register ra, int imm16) {
        super(rd, ra, imm16);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue ^ rbValueOrImm16;
    }

}

package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;

/**
 * Concrete implementation of {@link DuoDataInstruction} representing an ADD instruction.
 * */
public final class AddInstruction extends DuoDataInstruction {

    public AddInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb, boolean setFlag) {
        super(rd, ra, rb, setFlag);
    }

    public AddInstruction(@NotNull Register rd, @NotNull Register ra, int imm16, boolean setFlag) {
        super(rd, ra, imm16, setFlag);
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue + rbValueOrImm16;
    }

}

package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Concrete implementation of {@link SoloDataInstruction} representing a NOT instruction.
 * */
public final class NotInstruction extends SoloDataInstruction {

    public NotInstruction(@NotNull Register rd, @NotNull Register ra) {
        super(rd, ra);
    }

    public NotInstruction(@NotNull Register rd, int imm16) {
        super(rd, imm16);
    }

    @Override
    protected int operate(int raValueOrImm16) {
        return ~raValueOrImm16; //Set does not modify the data.
    }

    @Override
    public int opcode() {
        return 0b01001000000000000000000000000000;
    }

    @Override
    protected String getMnemonic() {
        return "!";
    }

}

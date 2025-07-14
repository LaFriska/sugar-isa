package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.ALUFlag;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents a goto instruction possibly with conditional execution.
 * */
public final class GotoInstruction extends SimpleInstruction{

    public final @Nullable ALUFlag flag;

    public GotoInstruction(int imm26) {
        this(imm26, null);
    }

    public GotoInstruction(int imm26, @Nullable ALUFlag flag) {
        super(imm26);
        this.flag = flag;
    }

    public GotoInstruction(@Nullable Register rd) {
        this(rd, null);
    }

    public GotoInstruction(@Nullable Register rd, @Nullable ALUFlag flag) {
        super(rd);
        this.flag = flag;
    }

    @Override
    public void execute(ArchitecturalState state) {
        if(flag == null || state.readFlag(flag)) {
            int newpc = format == Format.I ? imm26 : state.read(rd);
            state.write(Register.PC, newpc);
        }
    }

    @Override
    protected int opcode() {
        switch (flag){
            case N -> {
                return 0b10011000000000000000000000000000;
            }
            case Z -> {
                return 0b10100000000000000000000000000000;
            }
            case C -> {
                return 0b10101000000000000000000000000000;
            }
            case V -> {
                return 0b10110000000000000000000000000000;
            }
            default -> {
                return 0b10010000000000000000000000000000;
            }
        }
    }

}

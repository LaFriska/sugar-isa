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

    protected GotoInstruction(int imm26) {
        this(imm26, null);
    }

    protected GotoInstruction(int imm26, @Nullable ALUFlag flag) {
        super(imm26);
        this.flag = flag;
    }

    protected GotoInstruction(@Nullable Register rd) {
        this(rd, null);
    }

    protected GotoInstruction(@Nullable Register rd, @Nullable ALUFlag flag) {
        super(rd);
        this.flag = flag;
    }

    @Override
    protected void execute(ArchitecturalState state) {
        if(flag == null || state.readFlag(flag)) {
            int newpc = format == Format.I ? imm26 : state.read(rd);
            state.write(Register.PC, newpc);
        }
    }
}

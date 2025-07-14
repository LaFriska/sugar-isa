package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.ALUFlag;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents function calls.
 * */
public class CallInstruction extends SimpleInstruction {

    public final @Nullable ALUFlag flag;

    public CallInstruction(int imm26) {
        this(imm26, null);
    }

    public CallInstruction(int imm26, @Nullable ALUFlag flag) {
        super(imm26);
        this.flag = flag;
    }

    public CallInstruction(@Nullable Register rd) {
        this(rd, null);
    }

    public CallInstruction(@Nullable Register rd, @Nullable ALUFlag flag) {
        super(rd);
        this.flag = flag;
    }

    @Override
    public void execute(ArchitecturalState state) {
        if(flag == null || state.readFlag(flag)) { //conditional
            //Saves pc+4 to the link register.
            state.write(Register.LR, state.read(Register.PC) + 4);

            //Branching
            int newpc = format == Format.I ? imm26 : state.read(rd);
            state.write(Register.PC, newpc);
        }
    }

    @Override
    protected int opcode() {
        switch (flag){
            case N -> {
                return 0b10111000000000000000000000000000;
            }
            case Z -> {
                return 0b11000000000000000000000000000000;
            }
            case C -> {
                return 0b11001000000000000000000000000000;
            }
            case V -> {
                return 0b11010000000000000000000000000000;
            }
            default -> {
                return 0b11011000000000000000000000000000;
            }
        }
    }

}

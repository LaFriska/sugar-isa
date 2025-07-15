package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

public final class PopInstruction extends SimpleInstruction{

    public PopInstruction(int imm26) {
        super(imm26);
    }

    public PopInstruction(@Nullable Register rd) {
        super(rd);
    }

    @Override
    public void execute(ArchitecturalState state) {

        //writes value at stackpointer to rd
        state.write(rd, state.read(state.read(Register.SP)));

        //update stack pointer
        int newSp = state.read(Register.SP) + 4;
        state.write(Register.SP, newSp);

        //Increments pc
        ArchitecturalState.incrementPC(state);
    }

    @Override
    public int opcode() {
        return 0b10001000000000000000000000000000;
    }

    @Override
    protected String getMnemonic() {
        return "pop";
    }

}

package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.Nullable;

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
    }

}

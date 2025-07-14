package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.OffsetType;
import xyz.haroldgao.sugarisa.execute.Register;

public final class PushInstruction extends SimpleInstruction{

    public PushInstruction(int imm26) {
        super(imm26);
    }

    public PushInstruction(@Nullable Register rd) {
        super(rd);
    }

    @Override
    public void execute(ArchitecturalState state) {

        //update stack pointer
        int newSp = state.read(Register.SP) - 4;
        state.write(Register.SP, newSp);

        //writes value into memory
        state.write(state.read(newSp), format == Format.I ? imm26 : state.read(rd));

        //Increments pc
        ArchitecturalState.incrementPC(state);
    }

    @Override
    public int opcode() {
        return 0b10000000000000000000000000000000;
    }

}

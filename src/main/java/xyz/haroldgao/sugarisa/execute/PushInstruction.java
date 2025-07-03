package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.Nullable;

public class PushInstruction extends SimpleInstruction{

    public PushInstruction(int imm26) {
        super(imm26);
    }

    public PushInstruction(@Nullable Register rd) {
        super(rd);
    }

    @Override
    public void execute(ArchitecturalState state) {
        //TODO
    }
}

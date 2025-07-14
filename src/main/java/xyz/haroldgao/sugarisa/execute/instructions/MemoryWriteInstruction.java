package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.OffsetType;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents any memory write instruction.
 * */
public final class MemoryWriteInstruction extends MemoryInstruction{
    public MemoryWriteInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb, boolean setFlag, @NotNull OffsetType offsetType) {
        super(rd, ra, rb, setFlag, offsetType);
    }

    public MemoryWriteInstruction(@NotNull Register rd, @NotNull Register ra, int imm16, boolean setFlag, @NotNull OffsetType offsetType) {
        super(rd, ra, imm16, setFlag, offsetType);
    }

    @Override
    public void execute(ArchitecturalState state) {
        switch (offsetType){
            case STANDARD -> state.write(state.read(operateAndSetFlag(state)), state.read(rd));
            case PRE -> {
                state.write(ra, operateAndSetFlag(state)); //compute offset
                state.write(state.read(ra), state.read(rd)); //writes to memory
            }
            case POST -> {
                state.write(state.read(ra), state.read(rd)); //writes to memory
                state.write(ra, operateAndSetFlag(state)); //compute offset
            }
        }
        ArchitecturalState.incrementPC(state);
    }

    @Override
    public int opcode() {
        if(offsetType == OffsetType.STANDARD) return 0b01101000000000000000000000000000;
        return 0b01111000000000000000000000000000;
    }
}

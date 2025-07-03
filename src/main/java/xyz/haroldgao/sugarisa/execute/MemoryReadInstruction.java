package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;

/**
 * Represents any memory read instruction possibly with pre/post-offsets.
 * */
public final class MemoryReadInstruction extends MemoryInstruction{

    public MemoryReadInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb, boolean setFlag, OffsetType offsetType) {
        super(rd, ra, rb, setFlag, offsetType);
    }

    public MemoryReadInstruction(@NotNull Register rd, @NotNull Register ra, int imm16, boolean setFlag, OffsetType offsetType) {
        super(rd, ra, imm16, setFlag, offsetType);
    }

    @Override
    public void execute(ArchitecturalState state) {
        switch (offsetType){
            case STANDARD -> state.write(rd, state.read(operateAndSetFlag(state)));
            case PRE -> {
                state.write(ra, operateAndSetFlag(state));
                state.write(rd, state.read(ra));
            }
            case POST -> {
                state.write(rd, state.read(ra));
                state.write(ra, operateAndSetFlag(state));
            }
        }
    }
}

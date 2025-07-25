package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.OffsetType;
import xyz.haroldgao.sugarisa.execute.Register;

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
        ArchitecturalState.incrementPC(state);
    }

    @Override
    public int opcode() {
        if(offsetType == OffsetType.STANDARD) return 0b01100000000000000000000000000000;
        return 0b01110000000000000000000000000000;
    }

    @Override
    protected String getMnemonic() {
        if(offsetType == OffsetType.STANDARD) return "read";
        return offsetType.toString().toLowerCase() + "read";
    }

}

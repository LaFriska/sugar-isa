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
    protected int operateAndSetFlag(ArchitecturalState state) {
        int input1 = state.read(rd); //TODO extract duplicate
        int input2 = format == Format.I ? imm : state.read(rb);
        int result = operate(input1, input2);
        if(setFlag) setFlag(input1, input2, result, state);
        return result;
    }

    @Override
    public void execute(ArchitecturalState state) {
        switch (offsetType){
            case STANDARD -> {
                state.write(operateAndSetFlag(state), state.read(ra));
            }
            case PRE -> {
                state.write(rd, operateAndSetFlag(state)); //compute offset
                state.write(state.read(rd), state.read(ra)); //writes to memory
            }
            case POST -> {
                state.write(state.read(rd), state.read(ra)); //writes to memory
                state.write(rd, operateAndSetFlag(state)); //compute offset
            }
        }
        ArchitecturalState.incrementPC(state);
    }

    @Override
    public int opcode() {
        if(offsetType == OffsetType.STANDARD) return 0b01101000000000000000000000000000;
        return 0b01111000000000000000000000000000;
    }

    @Override
    protected String getMnemonic() {
        if(offsetType == OffsetType.STANDARD) return "write";
        return offsetType.toString().toLowerCase() + "write";
    }
}

package xyz.haroldgao.sugarisa.execute.instructions;

import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents an arbitrary instruction.
 * */
public abstract class Instruction {

    /**
     * Represents whether the instruction updates flag register.
     * */
    protected final boolean setFlag;

    protected Instruction(boolean setFlag) {
        this.setFlag = setFlag;
    }


    /**
     * Execution of the instruction given an architectural state. The execute method should be overridden to
     * modify the state accordingly.
     * */
    protected abstract void execute(ArchitecturalState state);

    /**
     * An opcode is the instruction-type identifier in the binary encoding of the instruction, for Sugar, it is
     * represented in the five most significant bits. This method returns an integer where the five most significant
     * bit is set to the correct opcode, while the other bits are set to 0.
     * */
    protected abstract int opcode();

    /**
     * Returns the binary encoding of the instruction.
     * */
    protected abstract int getBinary();

}

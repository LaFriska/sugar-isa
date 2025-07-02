package xyz.haroldgao.sugarisa.execute;

/**
 * Represents an arbitrary instruction.
 * */
abstract class Instruction {

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

}

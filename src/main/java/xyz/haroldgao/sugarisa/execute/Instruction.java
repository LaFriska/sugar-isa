package xyz.haroldgao.sugarisa.execute;

import xyz.haroldgao.sugarisa.ArchitecturalState;

/**
 * Represents an arbitrary instruction.
 * */
abstract class Instruction {

    /**
     * Execution of the instruction given an architectural state. The execute method should be overridden to
     * modify the state accordingly.
     * */
    protected abstract void execute(ArchitecturalState state);

}

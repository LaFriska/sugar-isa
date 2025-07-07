package xyz.haroldgao.sugarisa.execute.instructions;

import xyz.haroldgao.sugarisa.execute.Register;

/**
 * A set of commonly used {@link Instruction} singletons
 * */
public class Instructions {

    /**
     * NULL instruction representing one that does nothing.
     * */
    public static final Instruction NULL = new SetInstruction(Register.R0, Register.R0);

}

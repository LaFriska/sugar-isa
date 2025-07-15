package xyz.haroldgao.sugarisa.execute.instructions;

import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents an arbitrary instruction. Many field methods each instruction should have are defined as abstract methods
 * in this class. These include {@link Instruction#execute(ArchitecturalState)}, {@link Instruction#opcode()} and
 * {@link Instruction#getBinary()}. Equivalence is delegated to binary encodings.
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
    public abstract void execute(ArchitecturalState state);

    /**
     * An opcode is the instruction-type identifier in the binary encoding of the instruction, for Sugar, it is
     * represented in the five most significant bits. This method returns an integer where the five most significant
     * bit is set to the correct opcode, while the other bits are set to 0.
     * */
    public abstract int opcode();

    /**
     * Returns the binary encoding of the instruction.
     * */
    public abstract int getBinary();

    /**
     * Each instruction has a mnemonic, for the use of {@link Object#toString()}.
     * */
    protected abstract String getMnemonic();

    /**
     * Each instruction has a set of arguments, for the use of {@link Object#toString()}.
     * */
    protected abstract String[] getArgs();

    /**
     * A string representation in the form of "{mnemonic arg1, arg2, arg3}". For example,
     * {+ r1, r1, r3} is a valid string representation of the instruction "r1 += r3;".
     * Instructions which sets the flag register will have the character "f" prepended
     * to its string representation. For example, "f{| sp, lr, r11}" represents "sp = lr | r11 -> flag;".
     * */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(setFlag ? "f{" : "{");
        sb.append(getMnemonic().toLowerCase()).append(" ");
        String[] args = getArgs();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]);
            if(i != args.length - 1)
                sb.append(", ");
        }
        sb.append("}");
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Instruction i)) return false;
        return getBinary() == i.getBinary();
    }

    @Override
    public int hashCode() {
        return getBinary();
    }
}

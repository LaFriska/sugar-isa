package xyz.haroldgao.sugarisa.execute;

/**
 * Represents the architectural state, along with getters and setters for various parameters.
 * */
public interface ArchitecturalState {

    /**
     * Reads a value from a register.
     * @see Register
     * */
    int read(Register register);

    /**
     * Writes to a register.
     * @see Register
     * */
    void write(Register register, int value);

    /**
     * Reads from a memory address.
     * */
    int read(int address);

    /**
     * Writes to a memory address.
     */
    void write(int address, int value);

    /**
     * Mutates the flag register.
     * */
    void flag(int data);

    boolean readFlag(ALUFlag flag);

    /**
     * Increments program counter.
     * */
    static void incrementPC(ArchitecturalState state){
        state.write(Register.PC, state.read(Register.PC) + 4);
    }

}

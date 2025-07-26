package xyz.haroldgao.sugarisa.execute;

/**
 * Represents the architectural state, along with getters and setters for various parameters.
 * */
public interface ArchitecturalState {

    public enum CarryFlagMode{
        ADD,
        SUB,
        NONE
    }

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
     * Updates flag register given the inputs and results of a data processing operation.
     *
     * @param isArithmeticOperation whether flags overflow (V) and carry (C) should be updated depends on the type of
     *                              operation. If the instruction executing this method is an arithmetic operation, then
     *                              these flags should be updated.
     */
    default void flag(int input1,
                      int input2,
                      int result,
                      boolean isArithmeticOperation,
                      CarryFlagMode carryMode){ //TODO refactor its input
    }

    boolean readFlag(ALUFlag flag);

    /**
     * Increments program counter.
     * */
    static void incrementPC(ArchitecturalState state){
        state.write(Register.PC, state.read(Register.PC) + 4);
    }

}

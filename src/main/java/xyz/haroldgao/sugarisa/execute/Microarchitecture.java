package xyz.haroldgao.sugarisa.execute;

/**
 * Emulated microarchitecture.
 * */
class Microarchitecture implements ArchitecturalState {

    boolean hasTerminated = false;

    private final Memory memory;

    private final RegisterFile registerFile;

    Microarchitecture(Memory memory, RegisterFile registerFile){
        this.memory = memory;
        this.registerFile = registerFile;
    }

    @Override
    public int read(Register register) {
        return registerFile.read(register);
    }

    @Override
    public void write(Register register, int value) {
        if(register == Register.FL) return;
        if(register == Register.R0) return;
        registerFile.write(register, value);
    }

    @Override
    public int read(int address) {
        return memory.readWord(address);
    }

    @Override
    public void write(int address, int value) {
        memory.writeWord(address, value);
    }

    @Override
    public void flag(int data) {
        registerFile.write(Register.FL, data);
    }

    @Override
    public String getRegisterFileString() {
        return registerFile.toString();
    }

    @Override
    public boolean readFlag(ALUFlag flag) {
        if(flag == null) return false;
        int fl = read(Register.FL);
        return switch (flag){
            case N -> (fl & 0b1) == 0b1;
            case Z -> (fl & 0b10) == 0b10;
            case C -> (fl & 0b100) == 0b100;
            case V -> (fl & 0b1000) == 0b1000;
        };
    }

    @Override
    public void terminate() {
        hasTerminated = true;
    }
}

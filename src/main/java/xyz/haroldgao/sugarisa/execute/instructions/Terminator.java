package xyz.haroldgao.sugarisa.execute.instructions;

import xyz.haroldgao.sugarisa.execute.ArchitecturalState;

/**
 * The terminator is not a real instruction a user can input into assembly, but it is an instruction at the
 * end of each program to signal to the CPU to finish execution. Note that the parser is not responsible
 * for instantiating this instruction, but the loader is.
 * */
public class Terminator extends Instruction {

    static Terminator SINGLETON = null;

    private Terminator() {super(false);}

    @Override
    public void execute(ArchitecturalState state) {
        state.terminate();
    }

    @Override
    public int opcode() {
        return 0b11111000000000000000000000000000;
    }

    @Override
    public int getBinary() {
        return opcode();
    }

    @Override
    protected String getMnemonic() {
        return "term";
    }

    @Override
    protected String[] getArgs() {
        return new String[0];
    }

    public static Terminator getInstance(){
        if(SINGLETON == null)
            SINGLETON = new Terminator();
        return SINGLETON;
    }

}

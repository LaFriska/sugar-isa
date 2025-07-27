package xyz.haroldgao.sugarisa.execute;

import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.execute.instructions.Terminator;

/**
 * A loader takes a series of instructions and converts it to a memory instance with
 * instructions loaded into memory.
 * */
class Loader {

    private static Loader SINGLETON = null;

    private Loader(){}

    ArchitecturalState load(Instruction[] instructions){
        int pc = -4;
        Memory memory = new Memory();
        for(int i = 0; i < instructions.length; i++){
            pc = i * 4;
            memory.writeWord(pc, instructions[i].getBinary());
        }
        memory.writeWord(pc + 4, Terminator.getInstance().getBinary());

        return new Microarchitecture(memory, new RegisterFile());
    }

    Loader getSingleton(){
        if(SINGLETON == null)
            SINGLETON = new Loader();
        return SINGLETON;
    }

}

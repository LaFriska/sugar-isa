package xyz.haroldgao.sugarisa.execute;

import xyz.haroldgao.sugarisa.execute.instructions.Instruction;

/**
 * A loader takes a series of instructions and converts it to a memory instance with
 * instructions loaded into memory.
 * */
class Loader {

    private static Loader SINGLETON = null;

    private Loader(){}

    ArchitecturalState load(Instruction[] instructions){
        int pc;
        Memory memory = new Memory();
        for(int i = 0; i < instructions.length; i++){
            pc = i * 4;
            memory.writeWord(pc, instructions[i].getBinary());
        }
        return new Microarchitecture(memory, new RegisterFile());
    }

    Loader getSingleton(){
        if(SINGLETON == null)
            SINGLETON = new Loader();
        return SINGLETON;
    }

}

package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.execute.instructions.Terminator;
import xyz.haroldgao.sugarisa.parser.Parser;

import java.util.Iterator;

/**
 * This class represents a way to execute a set of instructions represented by Sugar assembly, which transforms
 * an assembly string using tokeniser, to parser, to loader and finally to the execution defined in this class.
 * */
public class SugarExecutor implements Iterator<ArchitecturalState> {

    private final String assembly;

    private Instruction nextInstruction = null;

    private ArchitecturalState astate;

    private SugarExecutor(@NotNull final String assembly){
        this.assembly = assembly;
        astate = Loader.getSingleton().load(Parser.parse(assembly).toArray(new Instruction[0]));
        nextInstruction = getNextInstruction();
    }

    private Instruction getNextInstruction(){
        return Decoder.getInstance().decode(astate.read(astate.read(Register.PC)));
    }

    /**
     * Loads Sugar assembly into an executor. Upon calling this method, the assembly will immediately be
     * tokenised, parsed, and loaded into a microarchitecture. The first instruction will be decoded and ready
     * for execution.
     * */
    public static SugarExecutor load(@NotNull final String assembly){
        return new SugarExecutor(assembly);
    }


    @Override
    public boolean hasNext() {
        return nextInstruction instanceof Terminator;
    }

    @Override
    public ArchitecturalState next() {
        if(hasNext()) return astate;
        nextInstruction.execute(astate);
        nextInstruction = getNextInstruction();
        return astate;
    }

    /**
     * Returns the content of the register file as string.
     * */
    public String getRegisterFileAsString(){
        return astate.getRegisterFileString();
    }

}

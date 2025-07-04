package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.tokeniser.Tokeniser;

import java.util.List;

/**
 * Parser for sugar assembly. Takes a {@link Tokeniser} as input and returns a series of
 * {@link Instruction}s.
 * */
public class Parser {

    private final Tokeniser tokeniser;

    public Parser(Tokeniser tokeniser){
        this.tokeniser = tokeniser;
    }

    public Parser(String assembly){
        this(new Tokeniser(assembly));
    }

    /**
     * Parses the assembly into a series of instructions.
     *
     * @throws ParseError if the assembly is incorrectly formatted.
     * */
    public List<Instruction> parse(){
        return null;
    }

}

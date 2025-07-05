package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.tokeniser.Token;
import xyz.haroldgao.sugarisa.tokeniser.TokenType;
import xyz.haroldgao.sugarisa.tokeniser.Tokeniser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Parser for sugar assembly. Takes a {@link Tokeniser} as input and returns a series of
 * {@link Instruction}s.
 * */
public class Parser implements Iterator<Instruction> {

    private final Tokeniser tokeniser;

    protected final String assembly;

    private Parser(Tokeniser tokeniser, String assembly){
        this.tokeniser = tokeniser;
        this.assembly = assembly;
    }

    /**
     * Parses assembly code into a series of instructions.
     *
     * @throws ParseError if the assembly is semantically incorrect.
     * */
    public static List<Instruction> parse(String assembly){
       ArrayList<Instruction> instructions = new ArrayList<>();
       Parser p = new Parser(new Tokeniser(assembly), assembly);
       while(p.hasNext()){
           instructions.add(p.next());
       }
       return instructions;
    }


    @Override
    public boolean hasNext() {
        return tokeniser.hasNext();
    }

    /**
     * Gets the next instruction.
     * */
    @Override
    public Instruction next() {
        if(!hasNext()) throw new NoMoreInstructionsError(this, "The given assembly code is either fully " +
                                                                           "parsed, or does not contain any instructions.");
        ArrayList<Token> tokens = new ArrayList<>();

        //Gets the next series of tokens until a terminator.
        while(tokeniser.hasNext()){
            Token t = tokeniser.next();
            tokens.add(t);
            if(t.type() == TokenType.TERM) break;
        }
        return tokensToInstruction(tokens.toArray(new Token[0]));
    }

    /**
     * Parses an array of tokens into an instruction.
     *
     * @throws ParseError if the token array does not properly represent an instruction.
     * */
    private Instruction tokensToInstruction(@NotNull Token[] tokens){

        if(tokens.length == 0) throw new NoMoreInstructionsError(this, "The given assembly code is either fully " +
                "parsed, or does not contain any instructions.");

        return null; //todo

    }

}

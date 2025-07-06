package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.tokeniser.Token;
import xyz.haroldgao.sugarisa.tokeniser.Tokeniser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static xyz.haroldgao.sugarisa.tokeniser.TokenType.*;

/**
 * Parser for sugar assembly. Takes a {@link Tokeniser} as input and returns a series of
 * {@link Instruction}s.
 * */
public class Parser implements Iterator<Instruction> {

    private @NotNull final Tokeniser tokeniser;

    protected @NotNull final String assembly;

    private @Nullable Token buffer;

    private int nextInstructionAddress = 0;

    /**
     * A map from labels to memory addresses for the program counter.
     * */
    private @NotNull final HashMap<String, Integer> linker;

    private Parser(@NotNull Tokeniser tokeniser, @NotNull String assembly){
        this.tokeniser = tokeniser;
        this.assembly = assembly;
        this.linker = new HashMap<>();
        nextToken();
        processComments();
    }

    /**
     * Parses assembly code into a series of instructions.
     *
     * @throws ParseError if the assembly is semantically incorrect.
     * */
    public static List<Instruction> parse(@NotNull String assembly){
       ArrayList<Instruction> instructions = new ArrayList<>();
       Parser p = new Parser(new Tokeniser(assembly), assembly);
       while(p.hasNext()){
           instructions.add(p.next());
       }
       return instructions;
    }


    /**
     * Checks whether there are more tokens to be parsed.
     * */
    /*
    * requires this.buffer == null || this.buffer.type != TokenType.COMMENT
    * */
    @Override
    public boolean hasNext() {
        return this.buffer != null;
    }

    /**
     * Gets the next instruction.
     * */
    /*
     * requires: this.buffer == null || this.buffer.type != TokenType.COMMENT
     * requires: the current buffer to hold the first token of the next instruction.
     * ensures: this.buffer == null || this.buffer.type != TokenType.COMMENT
     * */
    @Override
    public Instruction next() {
        if(!hasNext()) throw new NoMoreInstructionsError(this, "The given assembly code is either fully " +
                                                                           "parsed, or does not contain any instructions.");
        Token t = nextToken();
        if(t.type() == LABEL) processLabel(t.value());

        return null;
    }

    /**
     * Processes a label by registering the next instruction as the value of the label.
     * @throws DuplicateLabelException if the label already exists.
     * @throws UnclosedLabelException if the label does not end in a colon.
     * */
    private void processLabel(String label){
        if(linker.containsKey(label)) throw new DuplicateLabelException(this, label);
        Token t = nextToken();
        if(t.type() != COLON) throw new UnclosedLabelException(this, label);
        linker.put(label, nextInstructionAddress);
    }

    /**
     * Stores the next token in the tokeniser in a buffer. If the tokeniser
     * has no more tokens, sets the buffer to null.
     * @return the previous token held in the buffer.
     * */
    private Token nextToken(){
        var oldBuffer = buffer;
        if(tokeniser.hasNext()){
            buffer = tokeniser.next();
        }else{
            buffer = null;
        }
        return oldBuffer;
    }

    /**
     * Recursively move the buffer to the next token that is not a comment.
     * */
    private void processComments(){
        if(buffer != null && buffer.type() == COMMENT){
            nextToken();
            processComments();
        }
    }

    private Instruction returnInstruction(Instruction in){
        nextInstructionAddress += 4;
        return in;
    }

}

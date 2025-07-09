package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.Register;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.execute.instructions.Instructions;
import xyz.haroldgao.sugarisa.execute.instructions.MemoryWriteInstruction;
import xyz.haroldgao.sugarisa.tokeniser.Token;
import xyz.haroldgao.sugarisa.tokeniser.TokenType;
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
     * requires: the current buffer to hold the first token of the next <part> token.
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
     * Given that the parser currently points toward the first token of the next instruction,
     * parses the next instruction via the following CFG. This method also processes the terminator token.
     * */
    private Instruction parseInstruction(){ //<instruction> := <duo> | <solo> | <simple> | ε

        if(!hasNext()) return returnInstruction(Instructions.NULL);

        assert buffer != null;
        if(buffer.type() == TERM)
            return returnInstruction(Instructions.NULL);

        if(buffer.type() == KEYWORD){
            if(buffer.value() == null) throw new UnknownKeywordException(this, null); //TODO
            String keyword = buffer.value();

            //Instruction starts with register.
            if(Keywords.isRegister(keyword))
                return parseRegisterChangeInstruction();

        }else if(buffer.type() == LBRAC){
            return parseMemoryWriteInstruction();
        }
        throw new UnexpectedTokenError(this, buffer);
    }


    /**
     * A register change instruction is an instruction that starts with a register as its token.
     * */
    private Instruction parseRegisterChangeInstruction(){
        Token reg = nextToken();

        assert reg.type() == KEYWORD;

        if(!Register.containsToken(reg.value()))
            throw new RuntimeException("The tokeniser/parser is not implemented correctly. " +
                "This token: " + reg + " " +
                "should not be tokenised as a register keyword.");

        Register rd = Register.getFromToken(reg.value());

        //There are now two possible scenarios, the next token is an equal sign, or ALU shorthand like +=.

        if(buffer == null) throw new UnfinishedInstructionException(this);

        switch (buffer.type()){
            case EQ -> {return null;} //TODO
            case ADD_EQ -> {return null;} //TODO
            case SUB_EQ -> {return null;} //TODO
            case XOR_EQ -> {return null;} //TODO
            case OR_EQ -> {return null;} //TODO
            case MOD_EQ -> {return null;} //TODO
            case MUL_EQ -> {return null;} //TODO
            case DIV_EQ -> {return null;} //TODO
            case AND_EQ -> {return null;} //TODO
        }
        return null; //TODO
    }

    /**
     * Given that the buffer points at the first token of an instruction, attempts to parse the
     * next part as a memory write instruction.
     * @throws ParseError if the next part does not correctly represent a memory write instruction.
     * */
    private MemoryWriteInstruction parseMemoryWriteInstruction(){
        return null; //TODO
    }


    /**
     * Processes a label by registering the next instruction as the value of the label.
     * @throws DuplicateLabelException if the label already exists.
     * @throws UnclosedLabelException if the label does not end in a colon.
     * */
    private void processLabel(String label){
        if(linker.containsKey(label)) throw new DuplicateLabelException(this, label);
        Token t = nextToken();
        if(!testType(t, COLON)) throw new UnclosedLabelException(this, label);
        linker.put(label, nextInstructionAddress);
    }

    /**
     * Null-safe check of the type of an arbitrary token.
     * */
    private static boolean testType(Token t, @NotNull TokenType type){
        if(t == null) return false;
        return t.type() == type;
    }

    /**
     * Stores the next token in the tokeniser in a buffer. If the tokeniser
     * has no more tokens, sets the buffer to null. This method also skips comments by
     * calling {@link Parser#processComments()}.
     * @return the previous token held in the buffer.
     * */
    private Token nextToken(){
        var oldBuffer = buffer;
        if(tokeniser.hasNext()){
            buffer = tokeniser.next();
        }else{
            buffer = null;
        }
        processComments();
        return oldBuffer;
    }

    /**
     * Recursively move the buffer to the next token that is not a comment.
     * */
    private void processComments(){
        if(buffer != null && buffer.type() == COMMENT){
            buffer = tokeniser.hasNext() ? tokeniser.next() : null;
            processComments();
        }
    }

    private Instruction returnInstruction(Instruction in){
        nextInstructionAddress += 4;
        return in;
    }

}

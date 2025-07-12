package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.execute.Register;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.execute.instructions.NotInstruction;

import java.util.function.Function;

import static xyz.haroldgao.sugarisa.parser.ParseTreeUtils.*;
import static xyz.haroldgao.sugarisa.parser.ParseVariable.*;
import static xyz.haroldgao.sugarisa.tokeniser.TokenType.*;

/**
 * Here we place the messy construction of the parse tree in this class.
 * */
class SugarParseTree {

    static ParseTree getTerminator(Function<ParseState, Instruction> returnInstruction){
        return new ParseTree(
                eq(TERM),
                TRIVIAL_ON_ACCEPT,
                returnInstruction,
                TRIVIAL_ERROR
        );
    }

    static ParseTree START_NOT = new ParseTree(eq(NOT),
        new ParseTree(IS_REGISTER,
                SAVE_RD,
                getTerminator(p -> {
                    Register rd = (Register) p.get(RD);
                    return new NotInstruction(rd, rd);
                })
                )
    );

    /**
     * Gets the parse tree.
     * */
    static ParseTree get(){
        return null; //TODO
    }

}

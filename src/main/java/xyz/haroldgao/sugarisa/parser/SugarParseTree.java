package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.execute.Register;
import xyz.haroldgao.sugarisa.execute.instructions.*;
import xyz.haroldgao.sugarisa.tokeniser.TokenType;

import java.util.function.Function;

import static xyz.haroldgao.sugarisa.execute.Register.R0;
import static xyz.haroldgao.sugarisa.parser.ParseTreeUtils.*;
import static xyz.haroldgao.sugarisa.parser.ParseVariable.*;
import static xyz.haroldgao.sugarisa.tokeniser.TokenType.*;

/**
 * Here we place the messy construction of the parse tree in this class.
 * */
class SugarParseTree {



    static ParseTree COMPARE = new ParseTree(isSpecificKeyword("compare"),
            ra(comma(rb(term(
                    p -> new SubInstruction(R0, (Register) p.get(RA), (Register)  p.get(RB), true)
            ))))
    );

    /**
     * Subtree for the null instruction ";".
     * */
    static ParseTree NULL_INSTRUCTION = term(p -> Instructions.NULL);

    /**
     * Subtree for "return;" instructions.
     * */
    static ParseTree RETURN_INSTRUCTION = keyword(
            "return",
            term(
                    p -> Instructions.RETURN
            )
    );

    /**
     * Subtree for "!rd;" instructions.
     * */
    static ParseTree START_NOT = not(rd(term(p -> {
        Register rd = (Register) p.get(RD);
        return new NotInstruction(rd, rd);
    })));

    /**
     * Gets the parse tree.
     * */
    static ParseTree get(){

        ParseTree[] sugarSubtrees = new ParseTree[]{
                START_NOT,
                NULL_INSTRUCTION,
                RETURN_INSTRUCTION,
                COMPARE
        };

        return new ParseTree(
                TRUE,
                sugarSubtrees
        );
    }


    //-------------------------------------------Utility ----------------------------------------------------------

    static ParseTree rd(ParseTree... children){
        return new ParseTree(IS_REGISTER,
                SAVE_RD,
                children
        );
    }

    static ParseTree ra(ParseTree... children){
        return new ParseTree(IS_REGISTER,
                SAVE_RA,
                children
        );
    }

    static ParseTree rb(ParseTree... children){
        return new ParseTree(IS_REGISTER,
                SAVE_RB,
                children
        );
    }

    static ParseTree comma(ParseTree... children){
        return new ParseTree(eq(COMMA),
                children
        );
    }

    static ParseTree not(ParseTree... children){
        return new ParseTree(eq(NOT),
                children
        );
    }

    static ParseTree keyword(String word, ParseTree... children){
        return new ParseTree(
                isSpecificKeyword(word), children
        );
    }

    static ParseTree term(Function<ParseState, Instruction> returnInstruction){
        return new ParseTree(
                eq(TERM),
                TRIVIAL_ON_ACCEPT,
                returnInstruction,
                TRIVIAL_ERROR
        );
    }

}

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


    static ParseTree PUSH = keyword( //TODO add exception handling for when labels point to oversized PC address.
            "push",
            label(term(p -> new PushInstruction((Integer) p.get(IMM)))),
            rd(term(p -> new PushInstruction((Register) p.get(RD)))),
            imm26(term(p -> new PushInstruction((Integer) p.get(IMM))))
    );

    static ParseTree POP = keyword( //TODO add exception handling for when labels point to oversized PC address.
            "pop",
            label(term(p -> new PopInstruction((Integer) p.get(IMM)))),
            rd(term(p -> new PopInstruction((Register) p.get(RD)))),
            imm26(term(p -> new PopInstruction((Integer) p.get(IMM))))
    );


    /**
     * Gets the parse tree.
     * */
    static ParseTree get(){

        ParseTree[] sugarSubtrees = new ParseTree[]{
                START_NOT,
                NULL_INSTRUCTION,
                RETURN_INSTRUCTION,
                COMPARE,
                PUSH,
                POP
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

    static ParseTree label(ParseTree... children){
        return new ParseTree(IS_LABEL, SAVE_LABEL, TRIVIAL_RETURN_INST, p -> {
            if(p.fst().type() == LABEL) return new UnexpectedLabelError(p.fst().errorInfo(), p.fst().value());
            return null;
        }, children
        );
    }

    static ParseTree imm26(ParseTree... children){
        return imm(26, children);
    }

    private static ParseTree imm(int size, ParseTree... children){
        return new ParseTree(isUnsignedImmediate(size), SAVE_IMMEDIATE, TRIVIAL_RETURN_INST, p -> {
            if(TokenType.isImmediate(p.fst().type())) return new OversizedImmediateError(p.fst().errorInfo(), p.fst().value(), 26);
            return null;
        }, children
        );
    }

}

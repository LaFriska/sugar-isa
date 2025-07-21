package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.execute.OffsetType;
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

    //SIMPLE SUBTREES

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

    //SUBTREE STARTING WITH "["

    static ParseTree OFFSET_WRITE_SECOND_HALF = rbrac(
    equal(
        ra(
            term(p -> {
                if (p.get(RB) == null)
                    return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Integer) p.get(IMM), false, OffsetType.STANDARD);
                return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) p.get(RB), false, OffsetType.STANDARD);
            }),
            chain(
                keyword("flag",
                    term(p -> {
                        if (p.get(RB) == null)
                            return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Integer) p.get(IMM), true, OffsetType.STANDARD);
                        return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) p.get(RB), true, OffsetType.STANDARD);
                        })
                    )
                )
            )
        )
    );

    static ParseTree POST_OFFSET_WRITEN_CHAIN = new ParseTree(IS_RA); //TODO

    static ParseTree MEMORY_WRITE = lbrac(
    rd(
        add( //TODO add error here
            value(true, 15,
                    OFFSET_WRITE_SECOND_HALF
            )
        ).setErrorFunction(
                oversizedImmediate(15)
        )
            ,
        sub(
            imm15(
                    OFFSET_WRITE_SECOND_HALF
            ).setConsumer(SAVE_NEG_IMMEDIATE)
        ).setErrorFunction(
                oversizedImmediate(15)
        ),
        rbrac(
             equal(
                     ra(
                     term(
                          p -> new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) R0, false, OffsetType.STANDARD)
                     ),
                     chain(
                        keyword("flag",
                           term(p -> new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) R0, true, OffsetType.STANDARD))
                        ) //TODO add post offset here.
                     )
                 )
             )
        )
    ));


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
                POP,
                MEMORY_WRITE
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
        return specific(COMMA, children);
    }

    static ParseTree not(ParseTree... children){
        return specific(NOT, children);
    }

    static ParseTree lbrac(ParseTree... children){
        return specific(LBRAC, children);
    }

    static ParseTree rbrac(ParseTree... children){
        return specific(RBRAC, children);
    }

    static ParseTree add(ParseTree... children){
        return specific(ADD, children);
    }

    static ParseTree sub(ParseTree... children){
        return specific(SUB, children);
    }

    static ParseTree equal(ParseTree... children){
        return specific(EQ, children);
    }

    static ParseTree chain(ParseTree... children){
        return specific(CHAIN, children);
    }

    private static ParseTree specific(TokenType type, ParseTree... children){
        return new ParseTree(eq(type),
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

    /**
     * @param rb true then save to rb otherwise save to ra.
     * */
    static ParseTree value(boolean rb, int immediateSize, ParseTree... children){
        return new ParseTree(
                p -> (TokenType.isImmediate(p.fst().type()) && isUnsignedImmediate(immediateSize).test(p) ) || IS_REGISTER.test(p),
                p -> {
                    if(TokenType.isImmediate(p.fst().type())){
                        //assume correct size
                        SAVE_IMMEDIATE.accept(p);
                    }else if(rb){
                        SAVE_RB.accept(p);
                    }else{
                        SAVE_RA.accept(p);
                    }
                },
                TRIVIAL_RETURN_INST,
                children
        );
    }

    static ParseTree imm26(ParseTree... children){
        return imm(26, children);
    }

    static ParseTree imm15(ParseTree... children){
        return imm(15, children);
    }

    private static ParseTree imm(int size, ParseTree... children){
        return new ParseTree(isUnsignedImmediate(size), SAVE_IMMEDIATE, TRIVIAL_RETURN_INST, TRIVIAL_ERROR, children
        );
    }

}

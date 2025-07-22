package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.OffsetType;
import xyz.haroldgao.sugarisa.execute.Register;
import xyz.haroldgao.sugarisa.execute.instructions.*;
import xyz.haroldgao.sugarisa.tokeniser.TokenType;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

import static xyz.haroldgao.sugarisa.execute.Register.R0;
import static xyz.haroldgao.sugarisa.parser.ParseTreeUtils.*;
import static xyz.haroldgao.sugarisa.parser.ParseVariable.*;
import static xyz.haroldgao.sugarisa.tokeniser.TokenType.*;


/**
 * Here we place the messy construction of the parse tree in this class.
 */
final class SugarParseTree {

    //SIMPLE SUBTREES

    static ParseTree COMPARE = new ParseTree(isSpecificKeyword("compare"),
            ra(comma(rb(term(
                    p -> new SubInstruction(R0, (Register) p.get(RA), (Register) p.get(RB), true)
            ))))
    );

    /**
     * Subtree for the null instruction ";".
     */
    static ParseTree NULL_INSTRUCTION = term(p -> Instructions.NULL);

    /**
     * Subtree for "return;" instructions.
     */
    static ParseTree RETURN_INSTRUCTION = keyword(
            "return",
            term(
                    p -> Instructions.RETURN
            )
    );

    /**
     * Subtree for "!rd;" instructions.
     */
    static ParseTree START_NOT = not(rd(term(p -> {
        Register rd = (Register) p.get(RD);
        return new NotInstruction(rd, rd);
    })));


    static ParseTree PUSH = keyword( //TODO add exception handling for when labels point to oversized PC address.
            "push",
            label(term(p -> new PushInstruction((Integer) p.get(IMM)))),
            rd(term(p -> new PushInstruction((Register) p.get(RD)))),
            imm26(term(p -> new PushInstruction((Integer) p.get(IMM))))
    ).setErrorFunction(oversizedImmediate(26));

    static ParseTree POP = keyword( //TODO add exception handling for when labels point to oversized PC address.
            "pop",
            label(term(p -> new PopInstruction((Integer) p.get(IMM)))),
            rd(term(p -> new PopInstruction((Register) p.get(RD)))),
            imm26(term(p -> new PopInstruction((Integer) p.get(IMM))))
    ).setErrorFunction(oversizedImmediate(26));

    //SUBTREE STARTING WITH "["

    static ParseTree OFFSET_WRITE_SECOND_HALF = rbrac(
            equal(
                    ra(
                            term(p -> {
                                if (p.get(RB) == null)
                                    return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Integer) p.get(IMM), false, OffsetType.STANDARD);
                                return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) p.get(RB), false, OffsetType.STANDARD);
                            }),
                            chainflag(p -> {
                                if (p.get(RB) == null)
                                    return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Integer) p.get(IMM), true, OffsetType.STANDARD);
                                return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) p.get(RB), true, OffsetType.STANDARD);
                            })
                    )
            )
    );

    static ParseTree MEMORY_WRITE_POST_OFFSET = isRd(
            addeq(
                    value(true, 15,
                            term(
                                    p -> {
                                        if (p.get(RB) == null)
                                            return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Integer) p.get(IMM), false, OffsetType.POST);
                                        return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) p.get(RB), false, OffsetType.POST);
                                    }
                            ),
                            chainflag(
                                    p -> {
                                        if (p.get(RB) == null)
                                            return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Integer) p.get(IMM), true, OffsetType.POST);
                                        return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) p.get(RB), true, OffsetType.POST);
                                    }
                            )
                    )
            ).setErrorFunction(oversizedImmediate(15)),
            subeq(
                    imm15(
                            term(p -> new MemoryWriteInstruction(
                                    (Register) p.get(RD),
                                    (Register) p.get(RA),
                                    (Integer) p.get(IMM),
                                    false, OffsetType.POST)),

                            chainflag(
                                    p -> new MemoryWriteInstruction(
                                            (Register) p.get(RD),
                                            (Register) p.get(RA),
                                            (Integer) p.get(IMM),
                                            true, OffsetType.POST)
                            )

                    ).setConsumer(SAVE_NEG_IMMEDIATE)
            ).setErrorFunction(oversizedImmediate(15))
    );

    static ParseTree MEMORY_WRITE = lbrac(
            rd(
                    add( //TODO add error here
                            value(true, 15,
                                    OFFSET_WRITE_SECOND_HALF
                            )
                    ).setErrorFunction(oversizedImmediate(15))
                    ,
                    sub(
                            imm15(
                                    OFFSET_WRITE_SECOND_HALF
                            ).setConsumer(SAVE_NEG_IMMEDIATE)
                    ).setErrorFunction(oversizedImmediate(15)),
                    rbrac(
                            equal(
                                    ra(
                                            term(
                                                    p -> new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), R0, false, OffsetType.STANDARD)
                                            ),
                                            chain(
                                                    keyword("flag",
                                                            term(p -> new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), R0, true, OffsetType.STANDARD))
                                                    ),
                                                    MEMORY_WRITE_POST_OFFSET
                                            ).setErrorFunction(BAD_REGISTER)
                                    )
                            )
                    )
            )
    );


    //Construction of the most complex subtree, which is the subtree starting with a register.

    /**
     * Pre-offset read, syntax after first chain. Example: .... ra = [rd];
     */
    static ParseTree PRE_READ_SECOND_HALF = ra(equal(lbrac(isRd(rbrac(
            term(p -> {
                if (p.get(IMM) != null)
                    return new MemoryReadInstruction((Register) p.get(RA), (Register) p.get(RD), (Integer) p.get(IMM), false, OffsetType.PRE);
                return new MemoryReadInstruction((Register) p.get(RA), (Register) p.get(RD), (Register) p.get(RB), false, OffsetType.PRE);
            }),
            chainflag(
                    p -> {
                        if (p.get(IMM) != null)
                            return new MemoryReadInstruction((Register) p.get(RA), (Register) p.get(RD), (Integer) p.get(IMM), true, OffsetType.PRE);
                        return new MemoryReadInstruction((Register) p.get(RA), (Register) p.get(RD), (Register) p.get(RB), true, OffsetType.PRE);
                    }
            )
    )))));

    static ParseTree PRE_WRITE_SECOND_HALF = lbrac(isRd(rbrac(equal(ra(
            term(p -> {
                if (p.get(IMM) != null)
                    return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Integer) p.get(IMM), false, OffsetType.PRE);
                return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) p.get(RB), false, OffsetType.PRE);
            }),
            chainflag(
                    p -> {
                        if (p.get(IMM) != null)
                            return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Integer) p.get(IMM), true, OffsetType.PRE);
                        return new MemoryWriteInstruction((Register) p.get(RD), (Register) p.get(RA), (Register) p.get(RB), true, OffsetType.PRE);
                    }
            )
    ))))).setErrorFunction(BAD_REGISTER);


    static ParseTree START_REG = rd(
            simpleALUInstruction(MUL_EQ, MulInstruction.class),
            simpleALUInstruction(DIV_EQ, DivInstruction.class),
            simpleALUInstruction(MOD_EQ, ModInstruction.class),
            simpleALUInstruction(AND_EQ, AndInstruction.class),
            simpleALUInstruction(OR_EQ, OrInstruction.class),
            simpleALUInstruction(XOR_EQ, XorInstruction.class),
            simpleALUInstruction(LEFT_SHIFT, LeftShiftInstruction.class),
            simpleALUInstruction(RIGHT_SHIFT, RightShiftInstruction.class),


            new ParseTree(eq(ADD_EQ),
                    value(true, 16,
                            term(p -> createSimpleALUInstruction(AddInstruction.class, p, false)),
                            chain(
                                    keyword("flag", term(p -> createSimpleALUInstruction(AddInstruction.class, p, true))),
                                    PRE_READ_SECOND_HALF,
                                    PRE_WRITE_SECOND_HALF
                            ).setErrorFunction(BAD_REGISTER)
                    )
            ).setErrorFunction(oversizedImmediate(16)),

            new ParseTree(eq(SUB_EQ),
                    imm16(
                            term(p -> new SubInstruction((Register) p.get(RD), (Register) p.get(RD), (Integer) p.get(IMM), false)),
                            chain(
                                    keyword("flag", term(p -> new SubInstruction((Register) p.get(RD), (Register) p.get(RD), (Integer) p.get(IMM), true))),
                                    PRE_READ_SECOND_HALF.setConsumer(p -> {
                                        SAVE_RA.accept(p);
                                        NEGATE_IMMEDIATE.accept(p);
                                    }),
                                    PRE_WRITE_SECOND_HALF.setConsumer(p -> {
                                        SAVE_RA.accept(p);
                                        NEGATE_IMMEDIATE.accept(p);
                                    })
                            ).setErrorFunction(BAD_REGISTER)
                    ),
                    rb(
                            term(p -> new SubInstruction((Register) p.get(RD), (Register) p.get(RD), (Register) p.get(RB), false)),
                            chainflag(p -> new SubInstruction((Register) p.get(RD), (Register) p.get(RD), (Register) p.get(RB), true))
                    )
            )
    ).setErrorFunction(oversizedImmediate(16));


    //------------------------------------------------------------------------------------------------------------------

    /**
     * Returns the Sugar parse tree.
     */
    static ParseTree get() {

        ParseTree[] sugarSubtrees = new ParseTree[]{
                START_NOT,
                NULL_INSTRUCTION,
                RETURN_INSTRUCTION,
                COMPARE,
                PUSH,
                POP,
                MEMORY_WRITE,
                START_REG
        };

        return new ParseTree(
                TRUE,
                sugarSubtrees
        );
    }


    //-------------------------------------------Utility ----------------------------------------------------------

    static ParseTree chainflag(Function<ParseState, Instruction> returnInstruction) {
        return chain(keyword("flag", term(returnInstruction)));
    }

    static ParseTree rd(ParseTree... children) {
        return new ParseTree(IS_REGISTER,
                SAVE_RD,
                children
        );
    }

    static ParseTree ra(ParseTree... children) {
        return new ParseTree(IS_REGISTER,
                SAVE_RA,
                children
        );
    }

    static ParseTree isRa(ParseTree... children) {
        return new ParseTree(IS_RA, children);
    }

    static ParseTree isRd(ParseTree... children) {
        return new ParseTree(IS_RD, children);
    }

    static ParseTree rb(ParseTree... children) {
        return new ParseTree(IS_REGISTER,
                SAVE_RB,
                children
        );
    }

    /**
     * Returns the second-order subtree for simple ALU instructions, which are defined with
     * notationally sugar syntax. For instance, "r1 += r2;".
     */
    static ParseTree simpleALUInstruction(TokenType op, Class<? extends DuoDataInstruction> instructionClass) {
        return new ParseTree(eq(op),
                value(true, 16,
                        term(p -> createSimpleALUInstruction(instructionClass, p, false)),
                        chainflag(p -> createSimpleALUInstruction(instructionClass, p, true))
                )
        ).setErrorFunction(oversizedImmediate(16));
    }

    private static @NotNull DuoDataInstruction createSimpleALUInstruction(Class<? extends DuoDataInstruction> instructionClass, ParseState p, boolean flag) {
        if (p.get(RB) != null) {
            try {
                return instructionClass
                        .getDeclaredConstructor(Register.class, Register.class, Register.class, Boolean.class)
                        .newInstance((Register) p.get(RD), (Register) p.get(RD), (Register) p.get(RB), flag);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                return instructionClass
                        .getDeclaredConstructor(Register.class, Register.class, Integer.class, Boolean.class)
                        .newInstance((Register) p.get(RD), (Register) p.get(RD), (Integer) p.get(IMM), flag);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    static ParseTree comma(ParseTree... children) {
        return specific(COMMA, children);
    }

    static ParseTree not(ParseTree... children) {
        return specific(NOT, children);
    }

    static ParseTree lbrac(ParseTree... children) {
        return specific(LBRAC, children);
    }

    static ParseTree rbrac(ParseTree... children) {
        return specific(RBRAC, children);
    }

    static ParseTree add(ParseTree... children) {
        return specific(ADD, children);
    }

    static ParseTree addeq(ParseTree... children) {
        return specific(ADD_EQ, children);
    }

    static ParseTree subeq(ParseTree... children) {
        return specific(SUB_EQ, children);
    }

    static ParseTree sub(ParseTree... children) {
        return specific(SUB, children);
    }

    static ParseTree equal(ParseTree... children) {
        return specific(EQ, children);
    }

    static ParseTree chain(ParseTree... children) {
        return specific(CHAIN, children);
    }

    private static ParseTree specific(TokenType type, ParseTree... children) {
        return new ParseTree(eq(type),
                children
        );
    }

    static ParseTree keyword(String word, ParseTree... children) {
        return new ParseTree(
                isSpecificKeyword(word), children
        );
    }

    static ParseTree term(Function<ParseState, Instruction> returnInstruction) {
        return new ParseTree(
                eq(TERM),
                TRIVIAL_ON_ACCEPT,
                returnInstruction,
                TRIVIAL_ERROR
        );
    }

    static ParseTree label(ParseTree... children) {
        return new ParseTree(IS_LABEL, SAVE_LABEL, TRIVIAL_RETURN_INST, p -> {
            if (p.fst().type() == LABEL) return new UnexpectedLabelError(p.fst().errorInfo(), p.fst().value());
            return null;
        }, children
        );
    }

    /**
     * @param rb true then save to rb otherwise save to ra.
     */
    static ParseTree value(boolean rb, int immediateSize, ParseTree... children) {
        return new ParseTree(
                p -> (TokenType.isImmediate(p.fst().type()) && isUnsignedImmediate(immediateSize).test(p)) || IS_REGISTER.test(p),
                p -> {
                    if (TokenType.isImmediate(p.fst().type())) {
                        //assume correct size
                        SAVE_IMMEDIATE.accept(p);
                    } else if (rb) {
                        SAVE_RB.accept(p);
                    } else {
                        SAVE_RA.accept(p);
                    }
                },
                TRIVIAL_RETURN_INST,
                children
        );
    }

    static ParseTree imm26(ParseTree... children) {
        return imm(26, children);
    }

    static ParseTree imm15(ParseTree... children) {
        return imm(15, children);
    }

    static ParseTree imm16(ParseTree... children) {
        return imm(16, children);
    }

    private static ParseTree imm(int size, ParseTree... children) {
        return new ParseTree(isUnsignedImmediate(size), SAVE_IMMEDIATE, TRIVIAL_RETURN_INST, TRIVIAL_ERROR, children
        );
    }

    private SugarParseTree() {
    }

}

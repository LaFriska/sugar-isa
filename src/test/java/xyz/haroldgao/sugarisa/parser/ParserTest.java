package xyz.haroldgao.sugarisa.parser;

import org.junit.Assert;
import org.junit.Test;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;

/**
 * Tests parsing. These test cases will be designed to achieve maximal coverage with respect to the parse tree designed
 * in {@link SugarParseTree}. Note that {@link Instruction#toString()} will be used for these test cases.
 * */
public class ParserTest {

    /**
     * Tests stack operations.
     * */
    @Test
    public void testPushPop(){
        testInstructions("push r3;", "{push r3}");
        testInstructions("push 0xabcdef;", "{push 11259375}");
        testInstructions("push 0b1111000010100111;", "{push 61607}");
        testInstructions("push 12345;", "{push 12345}");

        testInstructions("pop lr;", "{pop lr}");
        testInstructions("pop 0xabcdef;", "{pop 11259375}");
        testInstructions("pop 0b1111000010100111;", "{pop 61607}");
        testInstructions("pop 12345;", "{pop 12345}");
    }


    /**
     * Tests the "compare ra, rb;" semantic.
     * */
    @Test
    public void testCompare(){
        testInstructions("compare r1, sp;", "f{- r0, r1, sp}");
        testInstructions("compare r1, sp;compare r2,r2;  compare  lr, r11;",
                "f{- r0, r1, sp}",
                "f{- r0, r2, r2}",
                "f{- r0, lr, r11}");
    }

    /**
     * Tests the "return;" semantic.
     * */
    @Test
    public void testReturn(){
        testInstructions("return;", "{goto lr}");
        testInstructions("return; return;return ;", "{goto lr}","{goto lr}","{goto lr}");
    }


    /**
     * Tests null instruction
     * */
    @Test
    public void testNull(){
        testInstructions(";", "{set r0, r0}");
        testInstructions("; ;;", "{set r0, r0}","{set r0, r0}","{set r0, r0}");
    }


    /**
     * Tests notational sugar for the not instruction.
     * */
    @Test
    public void testNotSimpleNotation(){
        testInstructions("!r3;",
                "{! r3, r3}"
        );
        testInstructions("!r3; !sp;",
                "{! r3, r3}",
                               "{! sp, sp}"
        );
        testInstructions("!r3; !sp; !sp; !lr; !r4;",
                "{! r3, r3}",
                "{! sp, sp}",
                "{! sp, sp}",
                "{! lr, lr}",
                "{! r4, r4}"
        );
    }

    /**
     * Tests branch instructions.
     * */
    @Test
    public void testBranching(){
        testInstructions("goto r3;", "{goto r3}");
        testInstructions("goto 0xa82ee;", "{goto 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    goto MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{goto 4}");

        testInstructions("goton r3;", "{goton r3}");
        testInstructions("goton 0xa82ee;", "{goton 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    goton MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{goton 4}");

        testInstructions("gotoz lr;", "{gotoz lr}");
        testInstructions("gotoz 0xa82ee;", "{gotoz 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    gotoz MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{gotoz 4}");

        testInstructions("gotoc lr;", "{gotoc lr}");
        testInstructions("gotoc 0xa82ee;", "{gotoc 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    gotoc MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{gotoc 4}");

        testInstructions("gotov lr;", "{gotov lr}");
        testInstructions("gotov 0xa82ee;", "{gotov 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    gotov MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{gotov 4}");


        testInstructions("call r3;", "{call r3}");
        testInstructions("call 0xa82ee;", "{call 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    call MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{call 4}");

        testInstructions("calln r3;", "{calln r3}");
        testInstructions("calln 0xa82ee;", "{calln 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    calln MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{calln 4}");

        testInstructions("callz lr;", "{callz lr}");
        testInstructions("callz 0xa82ee;", "{callz 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    callz MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{callz 4}");

        testInstructions("callc lr;", "{callc lr}");
        testInstructions("callc 0xa82ee;", "{callc 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    callc MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{callc 4}");

        testInstructions("callv lr;", "{callv lr}");
        testInstructions("callv 0xa82ee;", "{callv 688878}");
        testInstructions("""
                    ;
                MAIN:
                    ;
                    callv MAIN;
                """, "{set r0, r0}", "{set r0, r0}", "{callv 4}");

    }

    /**
     * Integration of simple subtrees.
     * */
    @Test
    public void integrateSimpleSubtrees(){
        testInstructions("!fl;;;",
                "{! fl, fl}","{set r0, r0}","{set r0, r0}"
        );
        testInstructions("!sp;;!r0;;;;",
                "{! sp, sp}","{set r0, r0}","{! r0, r0}","{set r0, r0}","{set r0, r0}","{set r0, r0}"
        );
        testInstructions("!r3;;return;return;;!       lr ;compare r1, r2;; return;",
                "{! r3, r3}",
                "{set r0, r0}",
                "{goto lr}",
                "{goto lr}",
                "{set r0, r0}",
                "{! lr, lr}",
                "f{- r0, r1, r2}",
                "{set r0, r0}",
                "{goto lr}"
        );
    }

    /**
     * Tests that linker and branches are correctly allocated.
     * */
    @Test
    public void testLinker(){
        testInstructions("""
                MAIN:
                    !r3;
                    !r4;
                BRANCH_1:
                    push 1;
                    push r1;
                BRANCH_2:
                    ;
                """, "{! r3, r3}", "{! r4, r4}", "{push 1}", "{push r1}", "{set r0, r0}");

        testInstructions("""
                MAIN:
                    !r3;
                    push BRANCH;
                    ;
                BRANCH:
                    ;
                    ;
                    pop MAIN;
                """, "{! r3, r3}", "{push 12}", "{set r0, r0}", "{set r0, r0}", "{set r0, r0}", "{pop 0}");
    }


    /**
     * Tests memory write with offset.
     */
    @Test
    public void testOffsetWrite(){
        testInstructions("[sp + lr] =  r3;", "{write sp, r3, lr}");
        testInstructions("[r5 + 0b101000101001011] =  r11;", "{write r5, r11, 20811}");
        testInstructions("[r6 + 0xabc] =  r10;", "{write r6, r10, 2748}");
        testInstructions("[r7 + 999] =  pc;", "{write r7, pc, 999}");
        testInstructions("[r0 - 123] =  lr;", "{write r0, lr, -123}");
        testInstructions("[r3 - 0xabc] =  sp;", "{write r3, sp, -2748}");
        testInstructions("[r5 - 0b101000101001011] =  r11;", "{write r5, r11, -20811}");
    }

    @Test
    public void testWriteNoOffset(){
        testInstructions("[r0] = r8;", "{write r0, r8, r0}");
        testInstructions("[r0] = r8 -> flag;", "f{write r0, r8, r0}");
        testInstructions("[r0] = r8 -> r0 += r3;", "{postwrite r0, r8, r3}");
        testInstructions("[r0] = r8 -> r0 += r3->flag;", "f{postwrite r0, r8, r3}");
        testInstructions("[r0] = r8 -> r0 += 0b100100001111011;", "{postwrite r0, r8, 18555}");
        testInstructions("[r0] = r8 -> r0 += 12345->flag;", "f{postwrite r0, r8, 12345}");
        testInstructions("[r0] = r8 -> r0 -= 0b100100001111011;", "{postwrite r0, r8, -18555}");
        testInstructions("[r0] = r8 -> r0 -= 12345->flag;", "f{postwrite r0, r8, -12345}");
    }

    @Test
    public void testALUSimpleNotation(){
        testInstructions("r3 += r4;", "{+ r3, r3, r4}");
        testInstructions("r3 -= r4;", "{- r3, r3, r4}");
        testInstructions("r3 *= r4;", "{* r3, r3, r4}");
        testInstructions("r3 /= r4;", "{/ r3, r3, r4}");
        testInstructions("r3 %= r4;", "{% r3, r3, r4}");
        testInstructions("r3 &= r4;", "{& r3, r3, r4}");
        testInstructions("r3 |= r4;", "{| r3, r3, r4}");
        testInstructions("r3 ^= r4;", "{^ r3, r3, r4}");
        testInstructions("r3 << r4;", "{<< r3, r3, r4}");
        testInstructions("r3 >> r4;", "{>> r3, r3, r4}");

        testInstructions("r3 += 12345;", "{+ r3, r3, 12345}");
        testInstructions("r3 -= 12345;", "{- r3, r3, 12345}");
        testInstructions("r3 *= 12345;", "{* r3, r3, 12345}");
        testInstructions("r3 /= 12345;", "{/ r3, r3, 12345}");
        testInstructions("r3 %= 12345;", "{% r3, r3, 12345}");
        testInstructions("r3 &= 12345;", "{& r3, r3, 12345}");
        testInstructions("r3 |= 12345;", "{| r3, r3, 12345}");
        testInstructions("r3 ^= 12345;", "{^ r3, r3, 12345}");
        testInstructions("r3 << 12345;", "{<< r3, r3, 12345}");
        testInstructions("r3 >> 12345;", "{>> r3, r3, 12345}");

        testInstructions("r3 += r4 -> flag;", "f{+ r3, r3, r4}");
        testInstructions("r3 -= r4 -> flag;", "f{- r3, r3, r4}");
        testInstructions("r3 *= r4 -> flag;", "f{* r3, r3, r4}");
        testInstructions("r3 /= r4 -> flag;", "f{/ r3, r3, r4}");
        testInstructions("r3 %= r4 -> flag;", "f{% r3, r3, r4}");
        testInstructions("r3 &= r4 -> flag;", "f{& r3, r3, r4}");
        testInstructions("r3 |= r4 -> flag;", "f{| r3, r3, r4}");
        testInstructions("r3 ^= r4 -> flag;", "f{^ r3, r3, r4}");
        testInstructions("r3 << r4 -> flag;", "f{<< r3, r3, r4}");
        testInstructions("r3 >> r4 -> flag;", "f{>> r3, r3, r4}");

        testInstructions("r3 += 12345 -> flag;", "f{+ r3, r3, 12345}");
        testInstructions("r3 -= 12345 -> flag;", "f{- r3, r3, 12345}");
        testInstructions("r3 *= 12345 -> flag;", "f{* r3, r3, 12345}");
        testInstructions("r3 /= 12345 -> flag;", "f{/ r3, r3, 12345}");
        testInstructions("r3 %= 12345 -> flag;", "f{% r3, r3, 12345}");
        testInstructions("r3 &= 12345 -> flag;", "f{& r3, r3, 12345}");
        testInstructions("r3 |= 12345 -> flag;", "f{| r3, r3, 12345}");
        testInstructions("r3 ^= 12345 -> flag;", "f{^ r3, r3, 12345}");
        testInstructions("r3 << 12345 -> flag;", "f{<< r3, r3, 12345}");
        testInstructions("r3 >> 12345 -> flag;", "f{>> r3, r3, 12345}");
    }

    @Test
    public void testPreRead(){
        testInstructions("r3 += pc -> r5 = [r3];", "{preread r5, r3, pc}");
        testInstructions("r1 += 0x1abc -> sp = [r1];", "{preread sp, r1, 6844}");
        testInstructions("r7 -= 5 -> r0 = [r7];", "{preread r0, r7, -5}");
        testInstructions("r3 += pc -> r5 = [r3] -> flag;", "f{preread r5, r3, pc}");
        testInstructions("r1 += 0x1abc -> sp = [r1] -> flag;", "f{preread sp, r1, 6844}");
        testInstructions("r7 -= 5 -> r0 = [r7] -> flag;", "f{preread r0, r7, -5}");
    }

    @Test
    public void testPreWrite(){
        testInstructions("r8 += pc -> [r8] = r5;", "{prewrite r8, r5, pc}");
        testInstructions("r3 += 0b101 -> [r3] = r5;", "{prewrite r3, r5, 5}");
        testInstructions("r3 -= 0b101 -> [r3] = r5;", "{prewrite r3, r5, -5}");
        testInstructions("r3 += pc -> [r3] = r5 -> flag;", "f{prewrite r3, r5, pc}");
        testInstructions("r3 += 0b001 -> [r3] = r5 -> flag;", "f{prewrite r3, r5, 1}");
        testInstructions("r3 -= 0b111 -> [r3] = r5 -> flag;", "f{prewrite r3, r5, -7}");
    }

    /**
     * Tests set instructions.
     * */
    @Test
    public void testSet(){
        testInstructions("r1 = r2;", "{set r1, r2}");
        testInstructions("r3 = 123;", "{set r3, 123}");
    }

    /**
     *
     * */
    @Test
    public void testNot(){
        testInstructions("r1 = !lr;", "{! r1, lr}");
        testInstructions("r1 = !5555;", "{! r1, 5555}");
    }

    @Test
    public void testALU(){
        testInstructions("r1 = r2 + r3;", "{+ r1, r2, r3}");
        testInstructions("r1 = r2 - r3;", "{- r1, r2, r3}");
        testInstructions("r1 = r2 * r3;", "{* r1, r2, r3}");
        testInstructions("r1 = r2 / r3;", "{/ r1, r2, r3}");
        testInstructions("r1 = r2 % r3;", "{% r1, r2, r3}");
        testInstructions("r1 = r2 | r3;", "{| r1, r2, r3}");
        testInstructions("r1 = r2 & r3;", "{& r1, r2, r3}");
        testInstructions("r1 = r2 << r3;", "{<< r1, r2, r3}");
        testInstructions("r1 = r2 >> r3;", "{>> r1, r2, r3}");
        testInstructions("r1 = r2 ^ r3;", "{^ r1, r2, r3}");

        testInstructions("r1 = r2 + r3 ->flag;", "f{+ r1, r2, r3}");
        testInstructions("r1 = r2 - r3->flag;", "f{- r1, r2, r3}");
        testInstructions("r1 = r2 * r3->flag;", "f{* r1, r2, r3}");
        testInstructions("r1 = r2 / r3->flag;", "f{/ r1, r2, r3}");
        testInstructions("r1 = r2 % r3->flag;", "f{% r1, r2, r3}");
        testInstructions("r1 = r2 | r3->flag;", "f{| r1, r2, r3}");
        testInstructions("r1 = r2 & r3->flag;", "f{& r1, r2, r3}");
        testInstructions("r1 = r2 << r3->flag;", "f{<< r1, r2, r3}");
        testInstructions("r1 = r2 >> r3->flag;", "f{>> r1, r2, r3}");
        testInstructions("r1 = r2 ^ r3->flag;", "f{^ r1, r2, r3}");
    }

    @Test
    public void testMemoryRead(){
        testInstructions("r10 = [r1];", "{read r10, r1, 0}");
        testInstructions("r10 = [r1]->flag;", "f{read r10, r1, 0}");
        testInstructions("r10 = [r1] -> r1 += r3;", "{postread r10, r1, r3}");
        testInstructions("r10 = [r1] -> r1 += 123;", "{postread r10, r1, 123}");
        testInstructions("r10 = [r1] -> r1 -= 123;", "{postread r10, r1, -123}");
        testInstructions("r10 = [r1] -> r1 += r3 -> flag;", "f{postread r10, r1, r3}");
        testInstructions("r10 = [r1] -> r1 += 123 -> flag;", "f{postread r10, r1, 123}");
        testInstructions("r10 = [r1] -> r1 -= 123 -> flag;", "f{postread r10, r1, -123}");

        testInstructions("r5 = [r2 + r3];", "{read r5, r2, r3}");
        testInstructions("r5 = [r2 + 4];", "{read r5, r2, 4}");
        testInstructions("r5 = [r2 - 4];", "{read r5, r2, -4}");

        testInstructions("r5 = [r2 + r3] -> flag;", "f{read r5, r2, r3}");
        testInstructions("r5 = [r2 + 4] -> flag;", "f{read r5, r2, 4}");
        testInstructions("r5 = [r2 - 4] -> flag;", "f{read r5, r2, -4}");
    }

    private void testInstructions(String assembly, String... expectedStrings){
        var actual = Parser.parse(assembly);
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(expectedStrings[i],actual.get(i).toString());
        }
    }

}

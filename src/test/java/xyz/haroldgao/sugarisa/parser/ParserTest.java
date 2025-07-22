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

    private void testInstructions(String assembly, String... expectedStrings){
        var actual = Parser.parse(assembly);
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(actual.get(i).toString(), expectedStrings[i]);
        }
    }

}

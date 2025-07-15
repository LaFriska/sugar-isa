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
        testInstructions("!r3;;return;return;;!       lr ;",
                "{! r3, r3}","{set r0, r0}","{goto lr}","{goto lr}","{set r0, r0}","{! lr, lr}"
        );
    }

    private void testInstructions(String assembly, String... expectedStrings){
        var actual = Parser.parse(assembly);
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(actual.get(i).toString(), expectedStrings[i]);
        }
    }

}

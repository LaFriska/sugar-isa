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

    private void testInstructions(String assembly, String... expectedStrings){
        var actual = Parser.parse(assembly);
        for (int i = 0; i < actual.size(); i++) {
            Assert.assertEquals(actual.get(i).toString(), expectedStrings[i]);
        }
    }

}

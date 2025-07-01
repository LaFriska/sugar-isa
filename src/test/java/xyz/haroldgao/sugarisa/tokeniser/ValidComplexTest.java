package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Tests tokenisation of assembly code that should not cause any exceptions to be thrown by the tokeniser.
 * The assembly code does not have to be correct, just syntactically valid at the token level.
 * Every test case here is complex.
 * */
public class ValidComplexTest extends TokeniserTest{

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        ArrayList<Object[]> data = new ArrayList<>();

        var expected = new String[]{
                "{LABEL, MAIN}",
                "{:}",
                "{KEYWORD, r1}",
                "{=}",
                "{KEYWORD, r2}",
                "{+}",
                "{KEYWORD, r3}",
                "{;}",
                "{KEYWORD, r4}",
                "{+=}",
                "{KEYWORD, r5}",
                "{;}",
                "{[}",
                "{KEYWORD, r6}",
                "{]}",
                "{=}",
                "{KEYWORD, r7}",
                "{;}",
        };

        data.add(new Object[]{
                """
                MAIN:
                    r1 = r2 + r3;
                    r4 += r5;
                    [r6] = r7;
                """,
                expected
        });

        data.add(new Object[]{
                """
                 \nMAIN       :
                    r1=r2+r3;r4 += r5;
                    [r6   ] =r7;
                """,
                expected
        });

        data.add(new Object[]{
                """
                 \nMAIN\t\t:r1=r2+r3;r4 += r5;[ r6]=r7
                 \n
                 \n
                 
                 ;
                """,
                expected
        });


        data.add(new Object[]{
                """
                0xaAbBcCdDeE123 0b1 999 + = - = [-7890],;:/= +=<<>>%=*=* ABC abc 11111a 11111A 11111_
                """,
                new String[]{
                        "{IMM_HEX, aAbBcCdDeE123}",
                        "{IMM_BIN, 1}",
                        "{IMM_DEC, 999}",
                        "{+}",
                        "{=}",
                        "{-}",
                        "{=}",
                        "{[}",
                        "{-}",
                        "{IMM_DEC, 7890}",
                        "{]}",
                        "{,}",
                        "{;}",
                        "{:}",
                        "{/=}",
                        "{+=}",
                        "{<<}",
                        "{>>}",
                        "{%=}",
                        "{*=}",
                        "{*}",
                        "{LABEL, ABC}",
                        "{KEYWORD, abc}",
                        "{KEYWORD, 11111a}",
                        "{LABEL, 11111A}",
                        "{LABEL, 11111_}",
                }
        });

        return data;
    }

}

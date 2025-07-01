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


    //This string was generated using ChatGPT.
    private static final String COMPLEX = """
            // Program Start
            INIT:\s
                r1 = 0x10;            // Hex immediate
                r2 = 25;              // Decimal immediate
                r3 = 0b1010;          // Binary immediate
                r4 = r1 + r2 -> flag; // ALU with flag
                r5 = r3 * r4;
                r6 = r5 / r2;
                r6 %= r1;
                r5 *= 0x2;
                r4 -= 1;
                r3 += r3;
                r2 = !r2;
                r1 = r4 & r5;
                r1 |= r3;
                r1 ^= r2;
                r7 = r1 << 2 -> flag;
                r7 = r7 >> 1;
            
                //////
                [r7] = r6;
                [0x20] = r6;
                [r7 + r6] = r5;
                [r7 + 4] = r4;
                [r7 - 8] = r3;
                r8 = [r7];
                r9 = [0x20];
                r10 = [r7 + r6];
                r11 = [r7 + 4];
                r1 = [r7 - 8];
            

                r7 += 4 -> [r7] = r6;
                r7 -= 2 -> [r7] = r5;
                r8 += r6 -> [r8] = r4;
                r9 += 0x10 -> r2 = [r9];
                r10 -= 4 -> r3 = [r10];
            
        
                [r7] = r6 -> r7 += 4;
                [r8] = r5 -> r8 += r6;
                r2 = [r9] -> r9 += 4;
                r3 = [r10] -> r10 += 0x8;
                r4 = [r11] -> r11 -= 2;
            
            
                [r7] = r6 -> r7 += 1 -> flag;
                r8 = [r7 + r6] -> flag;
            
       
                push r1, r2, r3;
                pop r1, r2, r3;
                push 0x55, 1234, 0b1;
                pop 0x55, 1234, 0b1;
            
      
                compare r7, r6;    
                goton CHECKPOINT;
                gotoz DONE;
                gotoc FUNC_CALL;
                gotov EXIT;
            
            CHECKPOINT:
                call FUNC_CALL;
                callv FUNC_CALL;
                return;
            
            FUNC_CALL:
                push lr;
                r1 = r1 + 1 -> flag;
                goto lr;
            
            DONE:
                ;   
                ;
                ;
                r0 = 0;  
            
            EXIT:
                goto lr;
            """;


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

        //This test case was generated using ChatGPT.
        data.add(new Object[]{
                COMPLEX,
                new String[]{
                        "{COMMENT,  Program Start}",
                        "{LABEL, INIT}", "{:}",
                        "{KEYWORD, r1}", "{=}", "{IMM_HEX, 10}", "{;}", "{COMMENT,  Hex immediate}",
                        "{KEYWORD, r2}", "{=}", "{IMM_DEC, 25}", "{;}",  "{COMMENT,  Decimal immediate}",
                        "{KEYWORD, r3}", "{=}", "{IMM_BIN, 1010}", "{;}",  "{COMMENT,  Binary immediate}",
                        "{KEYWORD, r4}", "{=}", "{KEYWORD, r1}", "{+}", "{KEYWORD, r2}", "{->}", "{KEYWORD, flag}", "{;}",  "{COMMENT,  ALU with flag}",
                        "{KEYWORD, r5}", "{=}", "{KEYWORD, r3}", "{*}", "{KEYWORD, r4}", "{;}",
                        "{KEYWORD, r6}", "{=}", "{KEYWORD, r5}", "{/}", "{KEYWORD, r2}", "{;}",
                        "{KEYWORD, r6}", "{%=}", "{KEYWORD, r1}", "{;}",
                        "{KEYWORD, r5}", "{*=}", "{IMM_HEX, 2}", "{;}",
                        "{KEYWORD, r4}", "{-=}", "{IMM_DEC, 1}", "{;}",
                        "{KEYWORD, r3}", "{+=}", "{KEYWORD, r3}", "{;}",
                        "{KEYWORD, r2}", "{=}", "{!}", "{KEYWORD, r2}", "{;}",
                        "{KEYWORD, r1}", "{=}", "{KEYWORD, r4}", "{&}", "{KEYWORD, r5}", "{;}",
                        "{KEYWORD, r1}", "{|=}", "{KEYWORD, r3}", "{;}",
                        "{KEYWORD, r1}", "{^=}", "{KEYWORD, r2}", "{;}",
                        "{KEYWORD, r7}", "{=}", "{KEYWORD, r1}", "{<<}", "{IMM_DEC, 2}", "{->}", "{KEYWORD, flag}", "{;}",
                        "{KEYWORD, r7}", "{=}", "{KEYWORD, r7}", "{>>}", "{IMM_DEC, 1}", "{;}",

                        "{COMMENT, ////}",
                        "{[}", "{KEYWORD, r7}", "{]}", "{=}", "{KEYWORD, r6}", "{;}",
                        "{[}", "{IMM_HEX, 20}", "{]}", "{=}", "{KEYWORD, r6}", "{;}",
                        "{[}", "{KEYWORD, r7}", "{+}", "{KEYWORD, r6}", "{]}", "{=}", "{KEYWORD, r5}", "{;}",
                        "{[}", "{KEYWORD, r7}", "{+}", "{IMM_DEC, 4}", "{]}", "{=}", "{KEYWORD, r4}", "{;}",
                        "{[}", "{KEYWORD, r7}", "{-}", "{IMM_DEC, 8}", "{]}", "{=}", "{KEYWORD, r3}", "{;}",
                        "{KEYWORD, r8}", "{=}", "{[}", "{KEYWORD, r7}", "{]}", "{;}",
                        "{KEYWORD, r9}", "{=}", "{[}", "{IMM_HEX, 20}", "{]}", "{;}",
                        "{KEYWORD, r10}", "{=}", "{[}", "{KEYWORD, r7}", "{+}", "{KEYWORD, r6}", "{]}", "{;}",
                        "{KEYWORD, r11}", "{=}", "{[}", "{KEYWORD, r7}", "{+}", "{IMM_DEC, 4}", "{]}", "{;}",
                        "{KEYWORD, r1}", "{=}", "{[}", "{KEYWORD, r7}", "{-}", "{IMM_DEC, 8}", "{]}", "{;}",

                        "{KEYWORD, r7}", "{+=}", "{IMM_DEC, 4}", "{->}", "{[}", "{KEYWORD, r7}", "{]}", "{=}", "{KEYWORD, r6}", "{;}",
                        "{KEYWORD, r7}", "{-=}", "{IMM_DEC, 2}", "{->}", "{[}", "{KEYWORD, r7}", "{]}", "{=}", "{KEYWORD, r5}", "{;}",
                        "{KEYWORD, r8}", "{+=}", "{KEYWORD, r6}", "{->}", "{[}", "{KEYWORD, r8}", "{]}", "{=}", "{KEYWORD, r4}", "{;}",
                        "{KEYWORD, r9}", "{+=}", "{IMM_HEX, 10}", "{->}", "{KEYWORD, r2}", "{=}", "{[}", "{KEYWORD, r9}", "{]}", "{;}",
                        "{KEYWORD, r10}", "{-=}", "{IMM_DEC, 4}", "{->}", "{KEYWORD, r3}", "{=}", "{[}", "{KEYWORD, r10}", "{]}", "{;}",

                        "{[}", "{KEYWORD, r7}", "{]}", "{=}", "{KEYWORD, r6}", "{->}", "{KEYWORD, r7}", "{+=}", "{IMM_DEC, 4}", "{;}",
                        "{[}", "{KEYWORD, r8}", "{]}", "{=}", "{KEYWORD, r5}", "{->}", "{KEYWORD, r8}", "{+=}", "{KEYWORD, r6}", "{;}",
                        "{KEYWORD, r2}", "{=}", "{[}", "{KEYWORD, r9}", "{]}", "{->}", "{KEYWORD, r9}", "{+=}", "{IMM_DEC, 4}", "{;}",
                        "{KEYWORD, r3}", "{=}", "{[}", "{KEYWORD, r10}", "{]}", "{->}", "{KEYWORD, r10}", "{+=}", "{IMM_HEX, 8}", "{;}",
                        "{KEYWORD, r4}", "{=}", "{[}", "{KEYWORD, r11}", "{]}", "{->}", "{KEYWORD, r11}", "{-=}", "{IMM_DEC, 2}", "{;}",

                        "{[}", "{KEYWORD, r7}", "{]}", "{=}", "{KEYWORD, r6}", "{->}", "{KEYWORD, r7}", "{+=}", "{IMM_DEC, 1}", "{->}", "{KEYWORD, flag}", "{;}",
                        "{KEYWORD, r8}", "{=}", "{[}", "{KEYWORD, r7}", "{+}", "{KEYWORD, r6}", "{]}", "{->}", "{KEYWORD, flag}", "{;}",

                        "{KEYWORD, push}", "{KEYWORD, r1}", "{,}", "{KEYWORD, r2}", "{,}", "{KEYWORD, r3}", "{;}",
                        "{KEYWORD, pop}", "{KEYWORD, r1}", "{,}", "{KEYWORD, r2}", "{,}", "{KEYWORD, r3}", "{;}",
                        "{KEYWORD, push}", "{IMM_HEX, 55}", "{,}", "{IMM_DEC, 1234}", "{,}", "{IMM_BIN, 1}", "{;}",
                        "{KEYWORD, pop}", "{IMM_HEX, 55}", "{,}", "{IMM_DEC, 1234}", "{,}", "{IMM_BIN, 1}", "{;}",

                        "{KEYWORD, compare}", "{KEYWORD, r7}", "{,}", "{KEYWORD, r6}", "{;}",
                        "{KEYWORD, goton}", "{LABEL, CHECKPOINT}", "{;}",
                        "{KEYWORD, gotoz}", "{LABEL, DONE}", "{;}",
                        "{KEYWORD, gotoc}", "{LABEL, FUNC_CALL}", "{;}",
                        "{KEYWORD, gotov}", "{LABEL, EXIT}", "{;}",

                        "{LABEL, CHECKPOINT}", "{:}",
                        "{KEYWORD, call}", "{LABEL, FUNC_CALL}", "{;}",
                        "{KEYWORD, callv}", "{LABEL, FUNC_CALL}", "{;}",
                        "{KEYWORD, return}", "{;}",

                        "{LABEL, FUNC_CALL}", "{:}",
                        "{KEYWORD, push}", "{KEYWORD, lr}", "{;}",
                        "{KEYWORD, r1}", "{=}", "{KEYWORD, r1}", "{+}", "{IMM_DEC, 1}", "{->}", "{KEYWORD, flag}", "{;}",
                        "{KEYWORD, goto}", "{KEYWORD, lr}", "{;}",

                        "{LABEL, DONE}", "{:}",
                        "{;}",
                        "{;}",
                        "{;}",
                        "{KEYWORD, r0}", "{=}", "{IMM_DEC, 0}", "{;}",

                        "{LABEL, EXIT}", "{:}",
                        "{KEYWORD, goto}", "{KEYWORD, lr}", "{;}",
                }
        });


        return data;
    }

}

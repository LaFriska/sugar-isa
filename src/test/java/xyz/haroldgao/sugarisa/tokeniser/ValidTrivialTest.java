package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Tests tokenisation of assembly code that should not cause any exceptions to be thrown by the tokeniser.
 * The assembly code does not have to be correct, just syntactically valid at the token level.
 * Every test case here is getData.
 * */
public class ValidTrivialTest extends TokeniserTest{

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        ArrayList<Object[]> data = new ArrayList<>();
        String[] simple = {
                "<<", ">>", "+=", "-=", "*=", "/=", "%=", "&=", "|=", "^=", "->",
                "=", ";", ":", "+", "-", "*", "/", "%", "&", "|", "^", "!", ",", "[", "]"
        };

        //Trivial cases
        for (String s : simple) {
            data.add(new Object[]{
                    s, new String[]{"{" + s + "}"}
            });
        }

        //Trivial cases with white space
        for (String s : simple) {
            data.add(new Object[]{
                    " \n \t" + s + "  \n\n  \f ", new String[]{"{" + s + "}"}
            });
        }

        //Labels
        data.add(new Object[]{
                "HELLO_WORLD_123",
                new String[]{
                        "{LABEL, HELLO_WORLD_123}"
                }
        });

        //Hex immediates
        data.add(new Object[]{
                "0x1234ABCD",
                new String[]{
                        "{IMM_HEX, 1234ABCD}"
                }
        });

        //Bin immediates
        data.add(new Object[]{
                "0b11001010",
                new String[]{
                        "{IMM_BIN, 11001010}"
                }
        });

        //Decimal immediates
        data.add(new Object[]{
                "123456789012",
                new String[]{
                        "{IMM_DEC, 123456789012}"
                }
        });

        //Comments
        data.add(new Object[]{
                "//This is a comment",
                new String[]{
                        "{COMMENT, This is a comment}"
                }
        });

        //Complex comments
        data.add(new Object[]{
                ": //This is another comment \n ->",
                new String[]{
                        "{:}",
                        "{COMMENT, This is another comment }",
                        "{->}"
                }
        });
        return data;
    }

}

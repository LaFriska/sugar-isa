package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Tests assembly code that should not cause any exceptions to be thrown by the tokeniser.
 * The assembly code does not have to be correct, just syntactically valid at the token level.
 * */
@RunWith(Parameterized.class)
public class ValidTokeniserTest {

    @Parameterized.Parameter(0)
    public String assembly; //Assembly code.

    @Parameterized.Parameter(1)
    public String[] tokenStrings; //String representation of tokens.

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

        return data;
    }

    @Test
    public void test(){
        Tokeniser tokeniser = new Tokeniser(assembly);
        int count = 0;
        while(tokeniser.hasNext()){
            Assert.assertEquals(tokenStrings[count], tokeniser.next().toString());
            count++;
        }
    }

}

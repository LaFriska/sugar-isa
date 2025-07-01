package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Abstract class representing an arbitrary set of test cases targetting {@link Tokeniser#next()}.
 * */
@RunWith(Parameterized.class)
public abstract class TokeniserTest {

    @Parameterized.Parameter(0)
    public String assembly; //Assembly code.

    @Parameterized.Parameter(1)
    public String[] tokenStrings; //String representation of tokens.

    @Test
    public void test(){
        Tokeniser tokeniser = new Tokeniser(assembly);
        int count = 0;
        while(tokeniser.hasNext()){
            Assert.assertEquals(tokenStrings[count], tokeniser.next().toString());
            count++;
        }
        Assert.assertEquals("The number of tokens extracted from the string buffer does not match " +
                        "                               the length of the expected string array.",
                                                count, tokenStrings.length);
    }

}

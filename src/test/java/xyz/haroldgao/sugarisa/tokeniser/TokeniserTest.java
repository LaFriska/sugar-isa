package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

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
    }

}

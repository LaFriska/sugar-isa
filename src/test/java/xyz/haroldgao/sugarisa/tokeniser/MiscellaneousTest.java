package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests miscellaneous functionalities of the tokeniser and edge cases.
 * */
public class MiscellaneousTest {

    /**
     * Tests empty case.
     * */
    @Test
    public void testEmpty(){
        Tokeniser empty1 = new Tokeniser("  \n \t ");
        Tokeniser empty2 = new Tokeniser("");
        Assert.assertFalse(empty1.hasNext());
        Assert.assertFalse(empty2.hasNext());
    }

}

package xyz.haroldgao.sugarisa.tokeniser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.*;

/**
 * Tests invalid assembly code that should cause an exception to be thrown.
 * */
@RunWith(Parameterized.class)
public class ExceptionTest {

    @Parameterized.Parameter()
    public Class<? extends TokenError> exceptionClass;

    @Parameterized.Parameter(1)
    public String assembly;

    @Parameterized.Parameter(2)
    public int iterationsUntilException;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        ArrayList<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
           TokenError.NoMoreTokensError.class,
           "", 1
        });

        data.add(new Object[]{
                TokenError.NoMoreTokensError.class,
                " ", 1
        });

        data.add(new Object[]{
                TokenError.NoMoreTokensError.class,
                "->", 2
        });

        data.add(new Object[]{
                TokenError.NoMoreTokensError.class,
                "[r5]", 4
        });

        data.add(new Object[]{
                TokenError.NoMoreTokensError.class,
                "+=+=/=-", 5
        });

        data.add(new Object[]{
                TokenError.UnexpectedSymbolError.class,
                "+=+=/=-\"", 5
        });

        data.add(new Object[]{
                TokenError.UnexpectedSymbolError.class,
                "hello worl$d", 3
        });

        data.add(new Object[]{
                TokenError.UnexpectedSymbolError.class,
                "~", 1
        });

        data.add(new Object[]{
                TokenError.UnexpectedSymbolError.class,
                "<", 1
        });

        data.add(new Object[]{
                TokenError.UnexpectedSymbolError.class,
                "hello -> <", 3
        });

        data.add(new Object[]{
                TokenError.UnexpectedSymbolError.class,
                ">", 1
        });

        data.add(new Object[]{
                TokenError.UnexpectedWordError.class,
                "Unexpected", 1
        });

        data.add(new Object[]{
                TokenError.UnexpectedWordError.class,
                "unexpected_2", 1
        });

        data.add(new Object[]{
                TokenError.UnexpectedWordError.class,
                "r2 = r1 + r_4 + +", 5
        });

        data.add(new Object[]{
                TokenError.UnexpectedWordError.class,
                "r2 = r1 + Rr4", 5
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "0xx", 1
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "0b10102", 1
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "0b10a01", 1
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "0b10A01;", 1
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "0b", 1
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "0x", 1
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "hello 0xx", 2
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "r1 = r2 + 0x123er ;", 5
        });

        data.add(new Object[]{
                TokenError.InvalidImmediateError.class,
                "r1 = r2 + 0b;", 5
        });

        return data;
    }

    @Test
    public void test(){
        Tokeniser tok = new Tokeniser(assembly);
        for (int i = 0; i < iterationsUntilException - 1; i++) {
            tok.next();
        }
        assertThrows(exceptionClass, tok::next);
    }

}

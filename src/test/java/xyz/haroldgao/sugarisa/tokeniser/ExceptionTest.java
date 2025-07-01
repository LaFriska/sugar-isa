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

    @Parameterized.Parameter(0)
    public Class<? extends TokenException> exceptionClass;

    @Parameterized.Parameter(1)
    public String assembly;

    @Parameterized.Parameter(2)
    public int iterationsUntilException;

    @Parameterized.Parameters
    public static Collection<Object[]> data(){
        ArrayList<Object[]> data = new ArrayList<>();

        data.add(new Object[]{
           TokenException.NoMoreTokensException.class,
           "", 1
        });

        data.add(new Object[]{
                TokenException.NoMoreTokensException.class,
                " ", 1
        });

        data.add(new Object[]{
                TokenException.NoMoreTokensException.class,
                "->", 2
        });

        data.add(new Object[]{
                TokenException.NoMoreTokensException.class,
                "[r5]", 4
        });

        data.add(new Object[]{
                TokenException.NoMoreTokensException.class,
                "+=+=/=-", 5
        });

        data.add(new Object[]{
                TokenException.UnexpectedSymbolException.class,
                "+=+=/=-\"", 5
        });

        data.add(new Object[]{
                TokenException.UnexpectedSymbolException.class,
                "hello worl$d", 3
        });

        data.add(new Object[]{
                TokenException.UnexpectedSymbolException.class,
                "~", 1
        });

        data.add(new Object[]{
                TokenException.UnexpectedSymbolException.class,
                "<", 1
        });

        data.add(new Object[]{
                TokenException.UnexpectedSymbolException.class,
                "hello -> <", 3
        });

        data.add(new Object[]{
                TokenException.UnexpectedSymbolException.class,
                ">", 1
        });

        data.add(new Object[]{
                TokenException.UnexpectedWordException.class,
                "Unexpected", 1
        });

        data.add(new Object[]{
                TokenException.UnexpectedWordException.class,
                "unexpected_2", 1
        });

        data.add(new Object[]{
                TokenException.UnexpectedWordException.class,
                "r2 = r1 + r_4 + +", 5
        });

        data.add(new Object[]{
                TokenException.UnexpectedWordException.class,
                "r2 = r1 + Rr4", 5
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
                "0xx", 1
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
                "0b10102", 1
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
                "0b10a01", 1
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
                "0b10A01;", 1
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
                "0b", 1
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
                "0x", 1
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
                "hello 0xx", 2
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
                "r1 = r2 + 0x123er ;", 5
        });

        data.add(new Object[]{
                TokenException.InvalidImmediateException.class,
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

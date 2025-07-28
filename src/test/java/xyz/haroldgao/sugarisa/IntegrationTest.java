package xyz.haroldgao.sugarisa;

import org.junit.Assert;
import org.junit.Test;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;
import xyz.haroldgao.sugarisa.execute.SugarExecutor;

import java.util.function.Predicate;

public class IntegrationTest {

    @Test
    public void testSetter(){
        test("r0 = 0xabc;", new int[]{0,0,0,0,0,0,0,0,0,0,0,0,0,0,4,0});
        test("r1 = 0xabc;", new int[]{0,0xabc,0,0,0,0,0,0,0,0,0,0,0,0,4,0});
        test("r1 = 0xabc;r0 = r1;", new int[]{0,0xabc,0,0,0,0,0,0,0,0,0,0,0,0,8,0});
        test("r1 = 0xabc;r2 = r1;", new int[]{0,0xabc,0xabc,0,0,0,0,0,0,0,0,0,0,0,8,0});
        test("r1 = 0xabc;pc=12;r2 = r1;r3=r1;", new int[]{0,0xabc,0,0xabc,0,0,0,0,0,0,0,0,0,0,16,0});
    }

    @Test
    public void testNot(){
        test("r1=7;!r1;", new int[]{0,-8,0,0,0,0,0,0,0,0,0,0,0,0,8,0});
    }

    public void test(String assembly, int[] registerFile){
        test(assembly, a -> {
            for (int i = 0; i < Register.values().length; i++) {
                if(registerFile[i] != a.read(Register.values()[i])) return false;
            }
            return true;
        });
    }

    public void test(String assembly, Predicate<ArchitecturalState> assertion){
        ArchitecturalState a = SugarExecutor.load(assembly).execute();
//        System.out.println(a.getRegisterFileString());
        Assert.assertTrue(a.getRegisterFileString(), assertion.test(a));
    }

}

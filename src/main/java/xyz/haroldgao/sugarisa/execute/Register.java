package xyz.haroldgao.sugarisa.execute;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a register in the Sugar ISA.
 * */
public enum Register {

    R0(0),
    R1(1),
    R2(2),
    R3(3),
    R4(4),
    R5(5),
    R6(6),
    R7(7),
    R8(8),
    R9(9),
    R10(10),
    R11(11),
    FL(12),
    LR(13),
    PC(14),
    SP(15);

    public final int id;

    public final String token;

    private static Map<String, Register> registerTokenMap = Arrays.stream(Register.values())
            .collect(Collectors.toMap(
                    (reg) -> reg.token,
                    (reg) -> reg
            ));

    Register(int id){
        this.id = id;
        this.token = this.toString().toLowerCase();
    }

    public static boolean containsToken(String token){
        return registerTokenMap.containsKey(token);
    }

    public static Register getFromToken(String token){
        return registerTokenMap.get(token);
    }

}

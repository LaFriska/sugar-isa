package xyz.haroldgao.sugarisa.parser;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * enumerates all the keywords.
 * */
enum Keywords {

    //Branching and conditions

    GOTO,
    GOTON,
    GOTOZ,
    GOTOC,
    GOTOV,
    CALL,
    CALLN,
    CALLZ,
    CALLC,
    CALLV,
    COMPARE,
    RETURN,

    //Other
    FLAG,

    //Stack

    PUSH,
    POP,

    //registers

    R0,
    R1,
    R2,
    R3,
    R4,
    R5,
    R6,
    R7,
    R8,
    R9,
    R10,
    R11,
    FL,
    LR,
    PC,
    SP;

    final String value;

    private static final Set<String> VALUE_SET = Arrays.stream(values())
                                                          .map((k) -> k.value)
                                                          .collect(Collectors.toSet());

    private final static Set<String> BRANCHING_SET =
            Arrays.stream(new Keywords[]{GOTO, GOTON, GOTOZ, GOTOC, GOTOV, CALL, CALLN,
                                         CALLZ, CALLC, CALLV, COMPARE, RETURN})
            .map(k -> k.value)
            .collect(Collectors.toSet());


    Keywords(){
        this.value = toString().toLowerCase();
    }

    private static boolean contains(String keyword){
        return VALUE_SET.contains(keyword);
    }

    private static boolean isBranching(String keyword){
        return BRANCHING_SET.contains(keyword);
    }

}

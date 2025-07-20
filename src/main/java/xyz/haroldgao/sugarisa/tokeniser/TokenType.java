package xyz.haroldgao.sugarisa.tokeniser;

import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a token type for Sugar assembly.
 * */
public enum TokenType {
    COMMENT(null),
    LABEL(null),
    IMM_HEX(null),
    IMM_DEC(null),
    IMM_BIN(null),
    KEYWORD(null),

    //Tokens with two characters
    LEFT_SHIFT("<<"),
    RIGHT_SHIFT(">>"),
    ADD_EQ("+="),
    SUB_EQ("-="),
    MUL_EQ("*="),
    DIV_EQ("/="),
    MOD_EQ("%="),
    AND_EQ("&="),
    OR_EQ("|="),
    XOR_EQ("^="),
    CHAIN("->"),

    //Tokens with a single character.
    EQ("="),
    TERM(";"),
    COLON(":"),
    ADD("+"),
    SUB("-"),
    MUL("*"),
    DIV("/"),
    MOD("%"),
    AND("&"),
    OR("|"),
    XOR("^"),
    NOT("!"),
    COMMA(","),
    LBRAC("["),
    RBRAC("]");


    //Array of tokens with two characters.
    public static final Set<TokenType> TWO_CHAR_TOKENS = filterBySize(2);

    //Array of tokens with a single character as its value.
    public static final Set<TokenType> SINGLE_CHAR_TOKENS = filterBySize(1);

    /**
     * Value of the token, if null, it means the value depends on the substring being processed.
     * */
    @Nullable
    final String value;

    TokenType(@Nullable String defaultValue){
        this.value = defaultValue;
    }

    static Set<TokenType> filterBySize(int size){
        return Arrays.stream(TokenType.values())
                .filter((t) -> t.value != null && t.value.length() == size)
                .collect(Collectors.toSet());
    }

    /**
     * Returns whether the given type is an immediate.
     * */
    public static boolean isImmediate(TokenType type){
        return type == IMM_BIN || type == IMM_HEX || type == IMM_DEC;
    }

}

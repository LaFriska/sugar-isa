package xyz.haroldgao.sugarisa.tokeniser;

import org.jetbrains.annotations.Nullable;

/**
 * Represents a token type for Sugar assembly.
 * */
public enum TokenType {
    LABEL(null),
    IMM_HEX(null),
    IMM_DEC(null),
    IMM_BIN(null),
    COMMENT(null),
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
    COMMA(","),
    LBRAC("["),
    RBRAC("]"),
    KEYWORD(null);

    /**
     * Value of the token, if null, it means the value depends on the substring being processed.
     * */
    @Nullable
    final String value;

    TokenType(@Nullable String defaultValue){
        this.value = defaultValue;
    }

}

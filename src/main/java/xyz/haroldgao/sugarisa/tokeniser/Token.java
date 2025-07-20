package xyz.haroldgao.sugarisa.tokeniser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.ErrorInfo;

import java.util.Objects;

public record Token(@NotNull TokenType type, @Nullable String value, @NotNull ErrorInfo errorInfo) {

    /**
     * Constructor without an explicit value will delegate the default value of the token type to the record.
     * */
    Token(TokenType type, @NotNull ErrorInfo errorInfo){
        this(type, type.value, errorInfo);
    }

    //-----------------------------------------------TESTING SPECIFIC CONSTRUCTORS--------------------------------------

    Token(TokenType type, @Nullable String value){
        this(type, value, ErrorInfo.TESTING);
    }

    Token(TokenType type){
        this(type, type.value);
    }

    /**
     * Equivalence is defined based on type and value, not the error info.
     * */
    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Token t)) return false;
        return t.type == type && Objects.equals(t.value, value);
    }

    @Override
    public String toString() {
        if(TokenType.SINGLE_CHAR_TOKENS.contains(type) || TokenType.TWO_CHAR_TOKENS.contains(type)){
            return "{" + type.value + "}";
        }
        return "{" + type + ", " + value + "}";
    }

    public String charPointers(){
        return errorInfo.pline() + ":" + errorInfo.pchar();
    }

}

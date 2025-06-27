package xyz.haroldgao.sugarisa.tokeniser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

record Token(@NotNull TokenType type, @Nullable String value) {

    Token(TokenType type){
        this(type, type.value);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Token t)) return false;
        return t.type == type && Objects.equals(t.value, value);
    }
}

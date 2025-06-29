package xyz.haroldgao.sugarisa.tokeniser;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class TokeniserUtils {
    static final Set<Character> HEX_CHARS = new HashSet<>(List.of('a','b','c','d','e','f'));

    /**
     * Given an arbitrary string, and a char-wise predicate, returns true if and only if
     * each character in the string satisfies the predicate.
     *
     * @param pred a predicate where each character in the string must satisfy for validity to hold.
     * */
    protected static boolean isValid(@NotNull String str, Predicate<Character> pred){
        if(str.isEmpty()) return true;
        for (int i = 0; i < str.length(); i++) {
            if(!pred.test(str.charAt(i)))
                return false;
        }
        return true;
    }

    /**
     * Returns whether the given character is a whitespace.
     * */
    protected static boolean isWhitespace(char c) {
        return c == ' ' || c == '\t' || c == '\n' || c == '\r' || c == '\f';
    }

    protected static boolean breaksAlphanumericalToken(char c){
        return !Character.isAlphabetic(c) && !Character.isDigit(c) && c != '_';
    }
}

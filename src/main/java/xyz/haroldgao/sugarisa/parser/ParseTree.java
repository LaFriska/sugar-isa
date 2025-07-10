package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.tokeniser.Token;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * This is a tree-structure representing the combinations of acceptable {@link Predicate<Token>} sequences that lead to
 * an instruction. The most common predicate is an equivalence of a token to a specific token type. All tokens must be
 * non-null. This tree is used in the {@link Parser}. This tree is also given the ability to read and write to an arbitrary instance
 * of {@link ParseState}, to simulate variable saving which can be accessed in resulting instructions.
 * */
record ParseTree(
        @NotNull    Predicate<Pair<Token, ParseState>> value,
        @NotNull    Consumer<ParseState> onAccept,
        @Nullable   Function<ParseState, Instruction> returnInstruction, //Returns null if not a leaf node.
        @NotNull    ParseTree[] subtrees
) {

    private static Function<ParseState, Instruction> NULL_FUNCTION = (a) -> null;

    private static Consumer<ParseState> DOES_NOTHING = (a) -> {};

    /**
     * Constructor for tree without any returned instruction.
     * */
    ParseTree(@NotNull    Predicate<Pair<Token, ParseState>> value,
              @NotNull    Consumer<ParseState> onAccept,
              @NotNull    ParseTree[] subtrees)
    {
        this(value, onAccept, NULL_FUNCTION, subtrees);
    }

    /**
     * Constructor for tree without any returned instruction.
     * */
    ParseTree(@NotNull    Predicate<Pair<Token, ParseState>> value,
              @NotNull    ParseTree[] subtrees)
    {
        this(value, DOES_NOTHING, (a) -> null, subtrees);
    }

    /**
     * Returns whether the current node is a leaf node.
     * */
    boolean isLeaf(){
        return subtrees.length == 0;
    }

    /**
     * Tests a token using the predicate.
     * */
    boolean test(Token token, ParseState parseState){
        return value.test(new Pair<>(token, parseState));
    }

    /**
     * Find and return the first subtree where a given token is accepted by the predicate.
     * @return the first accepted child, or null if none are accepted.
     * */
    ParseTree findAcceptableChild(Token token, ParseState parseState){
        for (@NotNull ParseTree subtree : subtrees) {
            if(subtree.test(token, parseState)) return subtree;
        }
        return null;
    }

}

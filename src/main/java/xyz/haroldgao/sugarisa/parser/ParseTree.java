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
        //Represents the node value, a predicate to test a token given a ParseState.
        @NotNull    Predicate<Pair<Token, ParseState>>              value,

        // A callback executed when the subtree is selected.
        @NotNull    Consumer<ParseState>                            onAccept,

        //Returns null if not a leaf node.
        @Nullable   Function<ParseState, Instruction>               returnInstruction,

        //A function which produces an error given a token and parseState. The resulting error is null if no error should be thrown.
        @NotNull    Function<Pair<Token, ParseState>, @Nullable ParseError>   errorFunction,

        //Children
        @NotNull    ParseTree[] subtrees
) {

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
     * Find and return the first subtree where a token given the parse state is accepted by the predicate.
     * @throws ParseError if no subtrees accept the token and the error function returns a nonnull value.
     * @return the first accepted child, or null if none are accepted and no errors are thrown.
     * */
    ParseTree run(Token token, ParseState parseState){
        for (@NotNull ParseTree subtree : subtrees) {
            if(subtree.test(token, parseState)) return subtree;
        }
        ParseError err =  errorFunction.apply(new Pair<>(token, parseState));
        if(err != null) throw err;
        return null;
    }

}

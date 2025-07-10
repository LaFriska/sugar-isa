package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.tokeniser.Token;

import java.util.function.Predicate;

/**
 * This is a tree-structure representing the combinations of acceptable {@link Predicate<Token>} sequences that lead to
 * an instruction. The most common predicate is an equivalence of a token to a specific token type. All tokens must be
 * non-null. This tree is used in the {@link Parser}. This tree is also given the ability to read and write to an arbitrary instance
 * of {@link ParseState}, to simulate variable saving which can be accessed in resulting instructions.
 * */
public class ParseTree {



}

package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.Register;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.tokeniser.Token;
import xyz.haroldgao.sugarisa.tokeniser.TokenType;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static xyz.haroldgao.sugarisa.execute.Register.*;
import static xyz.haroldgao.sugarisa.parser.ParseVariable.*;

public class ParseTreeUtils {

    static Function<Pair<Token, ParseState>, @Nullable ParseError> TRIVIAL_ERROR = pair -> null;
    static Function<ParseState, Instruction> TRIVIAL_RETURN_INST = state -> null;
    static Consumer<Pair<Token, ParseState>> TRIVIAL_ON_ACCEPT = p -> {};

    //-----------------------------------CONSUMERS-----------------------------------

    static Consumer<Pair<Token, ParseState>> SAVE_RD = (p) -> {
        p.snd().put(RD, Register.getFromToken(p.fst().value()));
    };

    static Consumer<Pair<Token, ParseState>> SAVE_RA = (p) -> {
        p.snd().put(RA, Register.getFromToken(p.fst().value()));
    };

    static Consumer<Pair<Token, ParseState>> SAVE_RB = (p) -> {
        p.snd().put(RB, Register.getFromToken(p.fst().value()));
    };

    //-----------------------------------PREDICATES----------------------------------

    static Predicate<Pair<Token, ParseState>> IS_REGISTER =
            (p) -> p.fst().type()
                    == TokenType.KEYWORD && Register.containsToken(p.fst().value());

    static Predicate<Pair<Token, ParseState>> TRUE = (p) -> true;

    static Predicate<Pair<Token, ParseState>> eq(@NotNull TokenType equivToken){
        return (p) -> p.fst().type() == equivToken;
    }

    static Predicate<Pair<Token, ParseState>> isSpecificKeyword(@NotNull String word){
        return (p) -> p.fst().type() == TokenType.KEYWORD && word.equals(p.fst().value());
    }

}

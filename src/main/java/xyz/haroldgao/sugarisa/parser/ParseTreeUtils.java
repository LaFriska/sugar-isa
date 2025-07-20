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
import static xyz.haroldgao.sugarisa.tokeniser.TokenType.LABEL;

public class ParseTreeUtils {

    static Function<Pair<Token, ParseState>, @Nullable ParseError> TRIVIAL_ERROR = pair -> null;
    static Function<ParseState, Instruction> TRIVIAL_RETURN_INST = state -> null;
    static Consumer<Pair<Token, ParseState>> TRIVIAL_ON_ACCEPT = p -> {};

    //-----------------------------------CONSUMERS-----------------------------------

    static Consumer<Pair<Token, ParseState>> SAVE_RD = (p) -> p.snd().put(RD, Register.getFromToken(p.fst().value()));

    static Consumer<Pair<Token, ParseState>> SAVE_LABEL = (p) -> p.snd().put(IMM, p.snd().getLink(p.fst().value()));

    static Consumer<Pair<Token, ParseState>> SAVE_RA = (p) -> p.snd().put(RA, Register.getFromToken(p.fst().value()));

    static Consumer<Pair<Token, ParseState>> SAVE_RB = (p) -> p.snd().put(RB, Register.getFromToken(p.fst().value()));

    /**
     * Assumes that the token value can be safely parsed into an immediate format.
     * */
    static Consumer<Pair<Token, ParseState>> SAVE_IMMEDIATE = p -> {
        assert p.fst().value() != null;
        p.snd().put(IMM, Integer.parseUnsignedInt(p.fst().value(), switch (p.fst().type()){
            case IMM_BIN -> 2;
            case IMM_HEX -> 16;
            default -> 10;
        }));
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



    static Predicate<Pair<Token, ParseState>> IS_LABEL = p -> p.fst().type() == LABEL && p.snd().hasLabel(p.fst().value());

    /**
     * Checks if the input token is an unsigned immediate.
     * */
    static Predicate<Pair<Token, ParseState>> isUnsignedImmediate(int bitsize){
        return p -> {

            //Must be an immediate type.
            if(p.fst().type() != TokenType.IMM_BIN && p.fst().type() != TokenType.IMM_DEC && p.fst().type() != TokenType.IMM_HEX) return false;

            if(p.fst().value() == null) return false;

            int immediate = switch (p.fst().type()) {
                case IMM_BIN -> Integer.parseUnsignedInt(p.fst().value(), 2);
                case IMM_HEX -> Integer.parseUnsignedInt(p.fst().value(), 16);
                default -> Integer.parseUnsignedInt(p.fst().value(), 10);
            };
            //Must be capped by bitsize.
            if(immediate >> bitsize != 0) return false;

            return true;
        };
    }

    private ParseTreeUtils(){}

}

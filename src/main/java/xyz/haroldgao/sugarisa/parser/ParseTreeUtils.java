package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.tokeniser.Token;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class ParseTreeUtils {

    static Function<Pair<Token, ParseState>, @Nullable ParseError> TRIVIAL_ERROR = pair -> null;
    static Function<ParseState, Instruction> TRIVIAL_RETURN_INST = state -> null;
    static Consumer<ParseState> TRIVIAL_ON_ACCEPT = state -> {};

    static Predicate<Pair<Token, ParseState>> predTypeEq(Token equivToken){
        return (p) -> p.fst().type() == equivToken.type();
    }

}

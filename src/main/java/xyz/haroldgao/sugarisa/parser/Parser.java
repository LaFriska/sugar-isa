package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.instructions.Instruction;
import xyz.haroldgao.sugarisa.tokeniser.Token;
import xyz.haroldgao.sugarisa.tokeniser.TokenType;
import xyz.haroldgao.sugarisa.tokeniser.Tokeniser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Parser for sugar assembly. Takes a {@link Tokeniser} as input and returns a series of
 * {@link Instruction}s.
 * */
public class Parser {

    /**
     * Parses assembly code into a series of instructions.
     *
     * @throws ParseError if the assembly is semantically incorrect.
     * */
    public static List<Instruction> parse(@NotNull String assembly){
       Tokeniser tokeniser = new Tokeniser(assembly);
       List<List<Token>> partition = new ArrayList<>();
       while(tokeniser.hasNext()){
           ArrayList<Token> current = new ArrayList<>();
           while(tokeniser.hasNext()){
               Token t = tokeniser.next();
               if(t.type() == TokenType.COMMENT) continue; //ignore comments
               current.add(t);
               if(t.type() == TokenType.TERM)
                   break;
           }
           partition.add(current);
       }
       HashMap<String, Integer> linker = Linker.link(partition);
       ArrayList<Instruction> result = new ArrayList<>();
        for (List<Token> tokens : partition) {
            result.add(parseSingleInstruction(tokens, linker));
        }
        return result;
    }

    private static Instruction parseSingleInstruction(@NotNull List<Token> tokens, @NotNull HashMap<String, Integer> linker){
        return null; //TODO
    }

}

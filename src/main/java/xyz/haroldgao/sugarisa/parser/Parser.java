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
 * Parser for a single instruction. However, a static method is provided to parse an assembly code in general.
 * */
public record Parser(@NotNull String assembly, List<Token> tokens, @NotNull HashMap<String, Integer> linker) {


    /**
     * Parses assembly code into a series of instructions.
     *
     * @throws ParseError if the assembly is semantically incorrect.
     * */
    public static List<Instruction> parse(@NotNull String assembly){
        List<List<Token>> partition = getTokensPartition(assembly);
        HashMap<String, Integer> linker = Linker.link(partition);
       ArrayList<Instruction> result = new ArrayList<>();
        for (List<Token> tokenList : partition) {
            Parser p = new Parser(assembly, tokenList, linker);
            result.add(p.parseInstruction());
        }

        return result;
    }

    private static @NotNull List<List<Token>> getTokensPartition(@NotNull String assembly) {
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
        return partition;
    }

    private Instruction parseInstruction(){
        ParseState parseState = new ParseState(linker);
        ParseTree current = SugarParseTree.get();
        int counter = 0;
        while(!current.isLeaf()){
            if(counter >= tokens.size()) throw new UnfinishedInstructionError(assembly); //Expect tokens but there are none left.
            Token t = tokens.get(counter);
            current = current.run(t, parseState);
            if(current == null) throw new UnexpectedTokenError(t);
            counter++;
        }
        return current.getInstruction(parseState);
    }

}

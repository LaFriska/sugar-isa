package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.tokeniser.Token;
import xyz.haroldgao.sugarisa.tokeniser.TokenType;

import java.util.HashMap;
import java.util.List;

class Linker {

    protected static HashMap<String, Integer> link(String assembly, List<List<Token>> tokens){
        HashMap<String, Integer> result = new HashMap<>();
        for (int i = 0; i < tokens.size(); i++) { //i * 4 is the pc address for the given instruction.
            List<Token> current = tokens.get(i);
            if(current.isEmpty()) throw new RuntimeException("This should not happen. One of the instruction token lists is empty.");
            Token tok = current.get(0);
            if(tok.type() != TokenType.LABEL) continue;

            //Label case
            String label = tok.value();
            if(result.keySet().contains(label)) throw new DuplicateLabelError(assembly, i, label);
            if(current.size() <= 1) throw new RuntimeException("This should not happen.");
            tok = current.get(1);
            if(tok.type() != TokenType.COLON) throw new UnclosedLabelError(assembly, i, label);
            //Label then colon
            current.remove(0);
            current.remove(1);
            result.put(label, i*4);
        }
        return result;
    }

}

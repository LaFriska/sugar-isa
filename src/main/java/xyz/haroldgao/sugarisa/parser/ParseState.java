package xyz.haroldgao.sugarisa.parser;

import java.util.HashMap;

/**
 * A map between {@link ParseVariable} to String values. Represents the intermediate state
 * while a parser runs through the decision tree.
 * */
class ParseState {

    private ParseState SINGLETON = null;

    private final HashMap<ParseVariable, String> map = new HashMap<>();

    private ParseState(){}

    protected void put(ParseVariable variable, String value){
        map.put(variable, value);
    }

    protected String get(ParseVariable variable){
        return map.get(variable);
    }

    protected ParseState getSingleton(){
        if(SINGLETON == null)
            SINGLETON = new ParseState();
        return SINGLETON;
    }

}

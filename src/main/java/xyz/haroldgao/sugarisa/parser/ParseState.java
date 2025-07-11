package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

/**
 * A map between {@link ParseVariable} to Objects. Represents the intermediate state
 * while a parser runs through the decision tree. This class also encapsulates the linker,
 * which links label strings to integer representation of program addresses.
 * */
class ParseState {

    /**
     * A map from labels to memory addresses for the program counter.
     * */
    private @NotNull final HashMap<String, Integer> linker = new HashMap<>();

    private final HashMap<ParseVariable, Object> map = new HashMap<>();

    protected void put(ParseVariable variable, String value){
        map.put(variable, value);
    }

    protected Object get(ParseVariable variable){
        return map.get(variable);
    }

    protected boolean hasLabel(String label){
        return linker.containsKey(label);
    }

    protected Integer getLink(String label){
        return linker.get(label);
    }

    protected void addLink(String label, int address){
        linker.put(label, address);
    }

}

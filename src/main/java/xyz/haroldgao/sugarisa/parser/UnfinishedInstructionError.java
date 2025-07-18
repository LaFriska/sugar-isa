package xyz.haroldgao.sugarisa.parser;

public class UnfinishedInstructionError extends ParseError {

    protected UnfinishedInstructionError(String assembly, int line) {
        super(assembly, line, "The assembly ended with an unfinished instruction.");
    }
}

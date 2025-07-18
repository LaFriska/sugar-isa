package xyz.haroldgao.sugarisa.parser;

public class UnfinishedInstructionException extends ParseError {

    protected UnfinishedInstructionException(String assembly, int line) {
        super(assembly, line, "The assembly ended with an unfinished instruction.");
    }
}

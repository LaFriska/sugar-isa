package xyz.haroldgao.sugarisa.parser;

public class UnfinishedInstructionException extends ParseError {

    protected UnfinishedInstructionException(Parser p) {
        super(p, "The assembly ended with an unfinished instruction.");
    }
}

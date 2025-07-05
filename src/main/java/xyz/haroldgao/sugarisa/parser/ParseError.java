package xyz.haroldgao.sugarisa.parser;

abstract class ParseError extends RuntimeException{
    protected ParseError(Parser p, String message){
        super("An error occurred parsing Sugar assembly code. " + message + "\n\n" + "Assembly Code:" + "\n" + p.assembly);
    }
}

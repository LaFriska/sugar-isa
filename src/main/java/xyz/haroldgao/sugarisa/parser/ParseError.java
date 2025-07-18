package xyz.haroldgao.sugarisa.parser;

abstract class ParseError extends RuntimeException{
    protected ParseError(String assembly, int line, String message){
        super("An error occurred parsing Sugar assembly code. The error occurred in instruction number " + line + "." + message + "\n\n" + "Assembly Code:" + "\n" + assembly);
    }
}

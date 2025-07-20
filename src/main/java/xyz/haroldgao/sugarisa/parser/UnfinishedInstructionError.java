package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.ErrorInfo;

public class UnfinishedInstructionError extends ParseError {

    protected UnfinishedInstructionError(String assembly) {
        super(null, "The assembly ended with an unfinished instruction." + "\n\n" + "Assembly Code:" + "\n" + assembly);
    }
}

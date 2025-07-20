package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.ErrorInfo;

public class OversizedImmediateError extends ParseError {

    protected OversizedImmediateError(ErrorInfo errorInfo, String imm, int bits) {
        super(errorInfo, "The following immediate value is too large: \"" + imm + "\". " +
                "This immediate should be a " + bits + "-bit value.");
    }
}

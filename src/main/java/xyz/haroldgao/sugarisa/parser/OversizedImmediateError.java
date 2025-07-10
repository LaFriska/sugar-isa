package xyz.haroldgao.sugarisa.parser;

public class OversizedImmediateError extends ParseError {

    protected OversizedImmediateError(Parser p, String imm, int bits) {
        super(p, "The following immediate value is too large: \"" + imm + "\". " +
                "This immediate should be a " + bits + "-bit value.");
    }
}

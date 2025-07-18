package xyz.haroldgao.sugarisa.parser;

public class OversizedImmediateError extends ParseError {

    protected OversizedImmediateError(String assembly, int line, String imm, int bits) {
        super(assembly, line, "The following immediate value is too large: \"" + imm + "\". " +
                "This immediate should be a " + bits + "-bit value.");
    }
}

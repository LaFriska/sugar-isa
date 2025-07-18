package xyz.haroldgao.sugarisa.parser;

class UnexpectedLabelError extends ParseError{
    protected UnexpectedLabelError(String assembly, int line, String label) {
        super(assembly, line, "An undefined label " + "\"" +label + "\" was found.");
    }
}

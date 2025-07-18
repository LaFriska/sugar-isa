package xyz.haroldgao.sugarisa.parser;

public class UnclosedLabelError extends ParseError {

  protected UnclosedLabelError(String assembly, int line, String label) {
    super(assembly, line, "The label " + "\"" + label + "\" does not end in a colon.");
  }
}

package xyz.haroldgao.sugarisa.parser;

public class UnclosedLabelException extends ParseError {

  protected UnclosedLabelException(String assembly, int line, String label) {
    super(assembly, line, "The label " + "\"" + label + "\" does not end in a colon.");
  }
}

package xyz.haroldgao.sugarisa.parser;

public class UnclosedLabelException extends ParseError {

  protected UnclosedLabelException(Parser p, String label) {
    super(p, "The label " + "\"" + label + "\" does not end in a colon.");
  }
}

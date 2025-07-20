package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.ErrorInfo;

public class UnclosedLabelError extends ParseError {

  protected UnclosedLabelError(ErrorInfo errorInfo, String label) {
    super(errorInfo, "The label " + "\"" + label + "\" does not end in a colon.");
  }
}

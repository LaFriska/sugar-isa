package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.tokeniser.Token;

public class UnexpectedTokenError extends ParseError {

  protected UnexpectedTokenError(String assembly, int line, Token t) {
    super(assembly, line, "An unexpected token has been found. Token: \"" + t.value() + "\".");
  }
}

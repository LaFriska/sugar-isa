package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.tokeniser.Token;

public class UnexpectedTokenError extends ParseError {

  protected UnexpectedTokenError(Parser p, Token t) {
    super(p, "An unexpected token has been found. Token: \"" + t.value() + "\".");
  }
}

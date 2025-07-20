package xyz.haroldgao.sugarisa.parser;

import xyz.haroldgao.sugarisa.ErrorInfo;
import xyz.haroldgao.sugarisa.tokeniser.Token;

public class UnexpectedTokenError extends ParseError {

  protected UnexpectedTokenError(Token t) {
    super(t.errorInfo(), "An unexpected token has been found. Token: \"" + t.value() + "\".");
  }
}

package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.ErrorInfo;

abstract class ParseError extends RuntimeException {
    protected ParseError(@Nullable ErrorInfo errorInfo, String message) {
        super(errorInfo != null ? "An error occurred while parsing Sugar assembly code in " + errorInfo.pline() + ":" + errorInfo.pchar() + "." + message + "\n\n" + "Assembly Code:" + "\n" + errorInfo.assembly()
                                : "An error occurred while parsing Sugar assembly code. " + message);
    }
}

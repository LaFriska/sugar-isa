package xyz.haroldgao.sugarisa.parser;

import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.ErrorInfo;

public class BadRegisterOffsetError extends ParseError{
    protected BadRegisterOffsetError(@Nullable ErrorInfo errorInfo) {
        super(errorInfo, "An erroneous attempt to pre/post-offset a register is made, " +
                "the register must be identical to that used for memory access in the same instruction.");
    }
}

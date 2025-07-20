package xyz.haroldgao.sugarisa;

import org.jetbrains.annotations.NotNull;

/**
 * Represents all information needed to be displayed in a tokeniser or parser error.
 * */
public record ErrorInfo(
        @NotNull String assembly,
        int pline,
        int pchar
) {

    public final static ErrorInfo TESTING = new ErrorInfo("",0,0);



}

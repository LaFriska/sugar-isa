package xyz.haroldgao.sugarisa.execute;

/**
 * Represents the type of memory offset.
 * */
public enum OffsetType {
    /**
     * Standard offset that does not modify register value.
     * */
    STANDARD,

    /**
     * Register value modified prior to memory instruction execution.
     * */
    PRE,

    /**
     * Register value modified after memory instruction execution.
     * */
    POST
}

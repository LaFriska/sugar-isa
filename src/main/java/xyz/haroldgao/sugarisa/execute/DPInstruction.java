package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a data-processing instruction.
 * */
abstract class DPInstruction extends Instruction {

    protected final @NotNull Format format;

    protected final @Nullable Integer imm16;

    protected final @NotNull Register rd;

    protected final @Nullable Register ra;

    protected final boolean isArithmeticOperation; //Whether the operation is arithmetic, false if logical.


    /**
     * Canonical constructor which sets isArithmeticOperation to the default false.
     * */
    protected DPInstruction(@NotNull Format format,
                            @Nullable Integer imm16,
                            @NotNull Register rd,
                            @Nullable Register ra,
                            boolean setFlag){
        this(format, imm16, rd, ra, setFlag, false);
    }

    /**
     * Canonical constructor, children of this class should delegate their respective constructors to this method.
     * */
    protected DPInstruction(@NotNull Format format,
                            @Nullable Integer imm16,
                            @NotNull Register rd,
                            @Nullable Register ra,
                            boolean setFlag,
                            boolean isArithmeticOperation){
        super(setFlag);
        this.format = format;
        this.imm16 = imm16;
        this.rd = rd;
        this.ra = ra;
        this.isArithmeticOperation = isArithmeticOperation;
    }

}

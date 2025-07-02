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

    /**
     * Canonical constructor, children of this class should delegate their respective constructors to this method.
     * */
    protected DPInstruction(@NotNull Format format,
                            @Nullable Integer imm16,
                            @NotNull Register rd,
                            @Nullable Register ra){
        this.format = format;
        this.imm16 = imm16;
        this.rd = rd;
        this.ra = ra;
    }

}

package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents a data instruction. Data instructions are defined as the union between memory instructions and
 * data processing instructions.
 * */
abstract class DataInstruction extends Instruction {

    protected final @NotNull Format format;

    protected final @Nullable Integer imm;

    protected final @NotNull Register rd;

    protected final @Nullable Register ra;

    protected final boolean isArithmeticOperation; //Whether the operation is arithmetic, false if logical.


    /**
     * Canonical constructor which sets isArithmeticOperation to the default false.
     * */
    protected DataInstruction(@NotNull Format format,
                              @Nullable Integer imm,
                              @NotNull Register rd,
                              @Nullable Register ra,
                              boolean setFlag){
        this(format, imm, rd, ra, setFlag, false);
    }

    /**
     * Canonical constructor, children of this class should delegate their respective constructors to this method.
     * */
    protected DataInstruction(@NotNull Format format,
                              @Nullable Integer imm,
                              @NotNull Register rd,
                              @Nullable Register ra,
                              boolean setFlag,
                              boolean isArithmeticOperation){
        super(setFlag);
        this.format = format;
        this.imm = imm;
        this.rd = rd;
        this.ra = ra;
        this.isArithmeticOperation = isArithmeticOperation;
    }

}

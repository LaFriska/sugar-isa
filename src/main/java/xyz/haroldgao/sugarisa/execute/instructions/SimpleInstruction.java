package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.OffsetType;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * An instruction is considered simple if it accepts a single rd/imm26 argument, along with a boolean value denoting
 * the format. These instructions always have setFlag set to false.
 * */
abstract class SimpleInstruction extends Instruction{

    protected final int imm26;

    protected final @Nullable Register rd;

    protected final Format format;

    /**
     * I-format constructor.
     * */
    protected SimpleInstruction(int imm26) {
        super(false);
        this.imm26 = imm26;
        this.rd = null;
        this.format = Format.I;
    }

    protected  SimpleInstruction(@Nullable Register rd){
        super(false);
        this.rd = rd;
        this.imm26 = 0;
        this.format = Format.R;
    }

    @Override
    protected int getBinary() {
        int f = rd == null ? 0b1 : 0b0;
        return opcode() | f << 26 | (rd == null ? imm26 : rd.id);
    }

}
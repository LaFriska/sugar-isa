package xyz.haroldgao.sugarisa.instructions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.ArchitecturalState;
import xyz.haroldgao.sugarisa.Register;

/**
 * Represents a data processing instructions that accepts TWO arguments, involving in rd, ra and rb/imm16.
 * */
abstract class DuoDPInstruction extends Instruction {

    private final @NotNull Format format;

    private final @Nullable Integer imm16;

    private final @Nullable Register rb;

    private final @NotNull Register rd;

    private final @NotNull Register ra;

    /**
     * R-format constructor.
     * */
    protected DuoDPInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb){
        format = Format.R;
        this.imm16 = null;
        this.rb = rb;
        this.rd = rd;
        this.ra = ra;
    }

    /**
     * I-format constructor.
     * */
    protected DuoDPInstruction(@NotNull Register rd, @NotNull Register ra, int imm16){
        format = Format.I;
        this.imm16 = imm16;
        this.rb = null;
        this.rd = rd;
        this.ra = ra;
    }


    /**
     * Defines the logical/arithmetic operation between ra and rb/imm16.
     *
     * @return the resulting destination register value.
     * */
    protected abstract int operate(int raValue, int rbValueOrImm16);


    /**
     * Execution simply defers to operation between ra and rb/imm16. This method is
     * marked as final since the specific ALU instruction depends on the execution only through
     * the operate method.
     * */
    @Override
    final public void execute(ArchitecturalState state) {
         state.write(rd, operate(state.read(ra), format == Format.I ? imm16 : state.read(rb)));
    }
}

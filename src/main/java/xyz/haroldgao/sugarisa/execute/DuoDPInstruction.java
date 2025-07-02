package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.ArchitecturalState;
import xyz.haroldgao.sugarisa.Register;

/**
 * Represents a data processing instructions that accepts TWO arguments, involving in rd, ra and rb/imm16.
 * */
abstract class DuoDPInstruction extends DPInstruction {

    private final @Nullable Register rb;

    /**
     * R-format constructor.
     * */
    protected DuoDPInstruction(@NotNull Register rd, @NotNull Register ra, @NotNull Register rb){
        super(Format.R, null, rd, ra);
        this.rb = rb;
    }

    /**
     * I-format constructor.
     * */
    protected DuoDPInstruction(@NotNull Register rd, @NotNull Register ra, int imm16){
        super(Format.I, imm16, rd, ra);
        this.rb = null;
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

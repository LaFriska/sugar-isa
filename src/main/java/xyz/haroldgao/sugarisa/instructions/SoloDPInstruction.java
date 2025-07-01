package xyz.haroldgao.sugarisa.instructions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.ArchitecturalState;
import xyz.haroldgao.sugarisa.Register;

/**
 * Represents a data processing instruction that accepts ONE argument, involving in rd, and ra/imm16.
 * */
public abstract class SoloDPInstruction extends Instruction{

    private final @NotNull Format format;

    private final @Nullable Integer imm16;

    private final @NotNull Register rd;

    private final @Nullable Register ra;


    /**
     * R-format constructor.
     * */
    protected SoloDPInstruction(@NotNull Register rd, @NotNull Register ra){
        format = Format.R;
        this.imm16 = null;
        this.rd = rd;
        this.ra = ra;
    }

    /**
     * I-format constructor.
     * */
    protected SoloDPInstruction(@NotNull Register rd, int imm16){
        format = Format.I;
        this.imm16 = imm16;
        this.rd = rd;
        this.ra = null;
    }

    /**
     * Defines the operation on ra before storing it in rd.
     *
     * @return the resulting destination register value.
     * */
    protected abstract int operate(int raValueOrImm16);

    /**
     * Execution delegates to the operate method, as specific instructions depends on concrete implementations only
     * through the operate method. This method is declared fine as no further overwriting is necessary.
     * */
    @Override
    final public void execute(ArchitecturalState state) {
        state.write(rd, operate(format == Format.R ? state.read(ra) : imm16));
    }
}

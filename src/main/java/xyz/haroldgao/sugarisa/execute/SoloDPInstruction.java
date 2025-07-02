package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a data processing instruction that accepts ONE argument, involving in rd, and ra/imm16.
 * */
public abstract class SoloDPInstruction extends DPInstruction {


    /**
     * R-format constructor.
     * */
    protected SoloDPInstruction(@NotNull Register rd, @NotNull Register ra){
        super(Format.R, null, rd, ra, false);
    }

    /**
     * I-format constructor.
     * */
    protected SoloDPInstruction(@NotNull Register rd, int imm16){
        super(Format.I, imm16, rd, null, false);
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

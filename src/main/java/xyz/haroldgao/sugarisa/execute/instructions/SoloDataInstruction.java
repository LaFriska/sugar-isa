package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents a data instruction that accepts ONE argument, involving in rd, and ra/imm16.
 * */
public abstract class SoloDataInstruction extends DataInstruction {


    /**
     * R-format constructor.
     * */
    protected SoloDataInstruction(@NotNull Register rd, @NotNull Register ra){
        super(Format.R, null, rd, ra, false);
    }

    /**
     * I-format constructor.
     * */
    protected SoloDataInstruction(@NotNull Register rd, int imm16){
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
        //Increments pc
        ArchitecturalState.incrementPC(state);
    }
}

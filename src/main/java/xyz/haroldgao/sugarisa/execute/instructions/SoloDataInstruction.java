package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents a data instruction that accepts ONE argument, involving in rd, and ra/imm16.
 * */
public abstract class SoloDataInstruction extends DataInstruction { //TODO change imm16 to imm22


    /**
     * R-format constructor.
     * */
    protected SoloDataInstruction(@NotNull Register rd, @NotNull Register ra){
        super(Format.R, null, rd, ra, false);
    }

    /**
     * I-format constructor.
     * */
    protected SoloDataInstruction(@NotNull Register rd, int imm22){
        super(Format.I, imm22, rd, null, false);
    }

    /**
     * Defines the operation on ra before storing it in rd.
     *
     * @return the resulting destination register value.
     * */
    protected abstract int operate(int raValueOrImm22);

    /**
     * Execution delegates to the operate method, as specific instructions depends on concrete implementations only
     * through the operate method. This method is declared fine as no further overwriting is necessary.
     * */
    @Override
    public void execute(ArchitecturalState state) {
        state.write(rd, operate(format == Format.R ? state.read(ra) : imm));
        //Increments pc
        ArchitecturalState.incrementPC(state);
    }

    @Override
    public int getBinary() {
        int f = ra == null ? 0b1 : 0b0;
        return opcode() | f << 26 | rd.id << 22 | (ra == null ? imm : ra.id);
    }

    @Override
    protected String[] getArgs() {
        return new String[]{
                rd.token, (ra == null ? String.valueOf(imm) : ra.token)
        };
    }
}

package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Concrete implementation of {@link SoloDataInstruction} representing a SET instruction.
 * */
public final class SetInstruction extends SoloDataInstruction {

    public SetInstruction(@NotNull Register rd, @NotNull Register ra) {
        super(rd, ra);
    }

    public SetInstruction(@NotNull Register rd, int imm22) {
        super(rd, imm22);
    }

    @Override
    protected int operate(int raValueOrImm16) {
        return raValueOrImm16; //Set does not modify the data.
    }

    @Override
    public int opcode() {
        return 0b00000000000000000000000000000000;
    }

    @Override
    protected String getMnemonic() {
        return "set";
    }

    @Override
    public void execute(ArchitecturalState state) {
        if(rd == Register.PC){
            //Does not increment PC
            state.write(Register.PC, format == Format.R ? state.read(ra) : imm);
        }else {
            super.execute(state);
        }
    }
}

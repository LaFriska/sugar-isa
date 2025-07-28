package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.OffsetType;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents any memory instruction.
 * */
abstract class MemoryInstruction extends DuoDataInstruction{

    protected @NotNull OffsetType offsetType;

    //isArithmeticOperation always set to true, since offsets are computed using ALU addition.
    protected MemoryInstruction(@NotNull Register rd,
                                @NotNull Register ra,
                                @NotNull Register rb,
                                boolean setFlag,
                                @NotNull OffsetType offsetType
    ) {
        super(rd, ra, rb, setFlag, true);
        this.offsetType = offsetType;
    }


    protected MemoryInstruction(@NotNull Register rd,
                                @NotNull Register ra,
                                int imm16, //TODO make it signed.
                                boolean setFlag,
                                @NotNull OffsetType offsetType
    ) {
        super(rd, ra, imm16, setFlag, true);
        this.offsetType = offsetType;
    }

    @Override
    protected int operate(int raValue, int rbValueOrImm16) {
        return raValue + rbValueOrImm16;
    }

    @Override
    public int getBinary() {
        if(offsetType == OffsetType.POST){
            return super.getBinary() | 0b1 << 16;
        }
        return super.getBinary();
    }

}

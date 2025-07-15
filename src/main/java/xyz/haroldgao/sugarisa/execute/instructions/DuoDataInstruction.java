package xyz.haroldgao.sugarisa.execute.instructions;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;

/**
 * Represents a data instructions that accepts TWO arguments, involving in rd, ra and rb/imm16.
 * */
abstract class DuoDataInstruction extends DataInstruction {

    private final @Nullable Register rb;

    /**
     * R-format constructor which sets isArithmeticOperation to false.
     * */
    protected DuoDataInstruction(@NotNull Register rd,
                                 @NotNull Register ra,
                                 @NotNull Register rb,
                                 boolean setFlag){
        this(rd, ra, rb, setFlag, false);
    }

    /**
     * R-format constructor.
     * */
    protected DuoDataInstruction(@NotNull Register rd,
                                 @NotNull Register ra,
                                 @NotNull Register rb,
                                 boolean setFlag,
                                 boolean isArithmeticOperation){
        super(Format.R, null, rd, ra, setFlag, isArithmeticOperation);
        this.rb = rb;
    }


    /**
     * I-format constructor which sets isArithmeticOperation to to false;
     * */
    protected DuoDataInstruction(@NotNull Register rd,
                                 @NotNull Register ra,
                                 int imm16,
                                 boolean setFlag
    ){
        this(rd, ra, imm16, setFlag, false);
    }


    /**
     * I-format constructor.
     * */
    protected DuoDataInstruction(@NotNull Register rd,
                                 @NotNull Register ra,
                                 int imm16,
                                 boolean setFlag,
                                 boolean isArithmeticOperation
    ){
        super(Format.I, imm16, rd, ra, setFlag, isArithmeticOperation);
        this.rb = null;
    }


    /**
     * Defines the logical/arithmetic operation between ra and rb/imm16.
     *
     * @return the resulting destination register value.
     * */
    protected abstract int operate(int raValue, int rbValueOrImm16);

    /**
     * Conducts the operation on the architectural state and sets the flag if necessary.
     * @return result of the operation.
     * */
    protected int operateAndSetFlag(ArchitecturalState state){
        int input1 = state.read(ra);
        int input2 = format == Format.I ? imm : state.read(rb);
        int result = operate(input1, input2);
        if(setFlag) state.flag(input1, input2, result, isArithmeticOperation);
        return result;
    }


    @Override
    public void execute(ArchitecturalState state) {
        state.write(rd, operateAndSetFlag(state));
        ArchitecturalState.incrementPC(state);
    }

    @Override
    public int getBinary() {
        int f = rb == null ? 0b1 : 0b0;
        int sf = setFlag ? 0b1 : 0b0;
        return opcode() | f << 26 | rd.id << 22 | sf << 21 | ra.id << 17 | (rb == null ? imm : rb.id);
    }

    @Override
    protected String[] getArgs() {
        return new String[]{
          rd.token, ra.token, (rb == null ? String.valueOf(imm) : rb.token)
        };
    }
}

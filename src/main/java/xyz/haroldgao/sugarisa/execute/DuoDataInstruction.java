package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * Execution simply defers to operation between ra and rb/imm16. This method is
     * marked as final since the specific ALU instruction depends on the execution only through
     * the operate method.
     * */
    @Override
    final public void execute(ArchitecturalState state) {
        int input1 = state.read(ra);
        int input2 = format == Format.I ? imm16 : state.read(rb);
        int result = operate(input1, input2);
        state.write(rd, result);
        if(setFlag) state.flag(input1, input2, result, isArithmeticOperation);
    }
}

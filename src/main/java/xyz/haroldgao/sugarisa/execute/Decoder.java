package xyz.haroldgao.sugarisa.execute;

import org.jetbrains.annotations.NotNull;
import xyz.haroldgao.sugarisa.execute.instructions.*;

/**
 * A decoder is responsible for reverse-engineering binaries back into Java representation of Sugar instructions.
 * A decoder can be though of as an instruction factory. Note that erroneous opcodes will result in the NULL instruction
 * being produced.
 * */
public class Decoder {

    private static Decoder SINGLETON = null;

    private Decoder(){}

    Instruction decode(int binary){
        return switch (getOpcode(binary)){
            case 0 -> getFormat(binary) == Format.I  ? new SetInstruction(getRd(binary), getImmediate(binary, 22))
                                                     : new SetInstruction(getRd(binary), getRb(binary));
            case 1 -> getFormat(binary) == Format.I  ? new AddInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new AddInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 2 -> getFormat(binary) == Format.I  ? new SubInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new SubInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 3 -> getFormat(binary) == Format.I  ? new MulInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new MulInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 4 -> getFormat(binary) == Format.I  ? new DivInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new DivInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 5 -> getFormat(binary) == Format.I  ? new ModInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new ModInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 6 -> getFormat(binary) == Format.I  ? new AndInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new AndInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 7 -> getFormat(binary) == Format.I  ? new OrInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new OrInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 8 -> getFormat(binary) == Format.I  ? new XorInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new XorInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 9 -> getFormat(binary) == Format.I  ? new NotInstruction(getRd(binary), getImmediate(binary, 22))
                                                     : new NotInstruction(getRd(binary), getRb(binary));
            case 10 -> getFormat(binary) == Format.I ? new LeftShiftInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new LeftShiftInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 11 -> getFormat(binary) == Format.I ? new RightShiftInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary))
                                                     : new RightShiftInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary));
            case 12 -> getFormat(binary) == Format.I ? new MemoryReadInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary), OffsetType.STANDARD)
                                                     : new MemoryReadInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary), OffsetType.STANDARD);
            case 13 -> getFormat(binary) == Format.I ? new MemoryWriteInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary), OffsetType.STANDARD)
                                                     : new MemoryWriteInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary), OffsetType.STANDARD);
            case 14 -> getFormat(binary) == Format.I ? new MemoryReadInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary), getP(binary))
                                                     : new MemoryReadInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary), getP(binary));
            case 15 -> getFormat(binary) == Format.I ? new MemoryWriteInstruction(getRd(binary), getRa(binary), getImmediate(binary, 16), getSf(binary), getP(binary))
                                                     : new MemoryWriteInstruction(getRd(binary), getRa(binary), getRb(binary), getSf(binary), getP(binary));
            case 16 -> getFormat(binary) == Format.I ? new PushInstruction(getImmediate(binary, 26)) : new PushInstruction(getRb(binary));
            case 17 -> getFormat(binary) == Format.I ? new PopInstruction(getImmediate(binary, 26)) : new PopInstruction(getRb(binary));
            case 18 -> getFormat(binary) == Format.I ? new GotoInstruction(getImmediate(binary, 26)) : new GotoInstruction(getRb(binary));
            case 19 -> getFormat(binary) == Format.I ? new GotoInstruction(getImmediate(binary, 26), ALUFlag.N) : new GotoInstruction(getRb(binary), ALUFlag.N);
            case 20 -> getFormat(binary) == Format.I ? new GotoInstruction(getImmediate(binary, 26), ALUFlag.Z) : new GotoInstruction(getRb(binary), ALUFlag.Z);
            case 21 -> getFormat(binary) == Format.I ? new GotoInstruction(getImmediate(binary, 26), ALUFlag.C) : new GotoInstruction(getRb(binary), ALUFlag.C);
            case 22 -> getFormat(binary) == Format.I ? new GotoInstruction(getImmediate(binary, 26), ALUFlag.V) : new GotoInstruction(getRb(binary), ALUFlag.V);
            case 23 -> getFormat(binary) == Format.I ? new CallInstruction(getImmediate(binary, 26)) : new GotoInstruction(getRb(binary));
            case 24 -> getFormat(binary) == Format.I ? new CallInstruction(getImmediate(binary, 26), ALUFlag.N) : new CallInstruction(getRb(binary), ALUFlag.N);
            case 25 -> getFormat(binary) == Format.I ? new CallInstruction(getImmediate(binary, 26), ALUFlag.Z) : new CallInstruction(getRb(binary), ALUFlag.Z);
            case 26 -> getFormat(binary) == Format.I ? new CallInstruction(getImmediate(binary, 26), ALUFlag.C) : new CallInstruction(getRb(binary), ALUFlag.C);
            case 27 -> getFormat(binary) == Format.I ? new CallInstruction(getImmediate(binary, 26), ALUFlag.V) : new CallInstruction(getRb(binary), ALUFlag.V);
            default -> Terminator.getInstance();
        };
    }

    private int getImmediate(int binary, int size){
        int bitmask = 0;
        for(int i = 0; i < size; i++){
            bitmask <<= 1;
            bitmask |= 1;
        }
        return binary & bitmask;
    }

    private int getOpcode(int binary){
        return (binary >> 27) & 0b11111;
    }

    private @NotNull Format getFormat(int binary){
        return ((binary >> 26) & 1) == 1 ? Format.I : Format.R;
    }

    private @NotNull Register getRd(int binary){
        return Register.values()[(binary >> 22) & 0b1111];
    }

    private @NotNull Register getRa(int binary){
        return Register.values()[(binary >> 17) & 0b1111];
    }

    private @NotNull Register getRb(int binary){
        return Register.values()[binary & 0b1111];
    }

    private boolean getSf(int binary){
        return ((binary >> 21) & 1) == 1;
    }

    private OffsetType getP(int binary){
        return ((binary >> 16) & 1) == 1 ? OffsetType.POST : OffsetType.PRE;
    }

    static Decoder getInstance(){
        if(SINGLETON == null)
            SINGLETON = new Decoder();
        return SINGLETON;
    }

}

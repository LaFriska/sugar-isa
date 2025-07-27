package xyz.haroldgao.sugarisa.execute;

/**
 * Register file is simply an array of integers.
 * */
class RegisterFile {

    private static final String[] REGISTER_NAMES = new String[]{
            "r0","r1","r2","r3","r4","r5","r6","r7","r8","r9","r10","r11","fl","lr","pc","sp"
    };

    int[] internal;

    RegisterFile(){
        internal = new int[15];;
    }

    int read(Register register){
        return internal[register.id];
    }

    void write(Register register, int data){
        internal[register.id] = data;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Register File: \n");
        for (int i = 0; i < internal.length; i++) {
            sb.append(REGISTER_NAMES[i]).append(": ").append(internal[i]).append("\n");
        }
        return sb.toString();
    }
}

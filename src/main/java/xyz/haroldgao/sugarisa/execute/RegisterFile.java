package xyz.haroldgao.sugarisa.execute;

/**
 * Register file is simply an array of integers.
 * */
class RegisterFile {

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

}

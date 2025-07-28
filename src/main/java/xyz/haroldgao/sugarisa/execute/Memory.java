package xyz.haroldgao.sugarisa.execute;

import java.util.HashMap;

/**
 * An emulator for the Sugar ISA memory, with maximum 4GB.
 * Since the memory in Sugar ISA is byte-addressable with 32-bit addressability,
 * a naive array of bytes will allocate 4GB of memory on any computers running the emulator.
 * This is very inefficient. To resolve this issue, a hashmap between integers and byte arrays of no
 * more than 2^16 elements is used, which grows as more "chunks" of memory is being used. Although
 * in theory, more than 4GB must be occupied for worst case scenarios, it is assumed that most
 * uses of this emulator will likely use a small portion of the memory address space.
 * Please also note that Sugar uses little endian.
 * */
final class Memory {

    private final HashMap<Integer, byte[]> internal;

    private static final int ARRAY_INDEX_BITMASK = 0xFFFF;

    private static final int BYTE_ARRAY_SIZE = 65536;

    Memory(){
        internal = new HashMap<>();
    }

    /**
     * Reads a single byte from memory.
     * */
    byte read(int address){
        int key = address >> 16;
        if(!internal.containsKey(key)) return 0; //No allocated
        return internal.get(key)[address & ARRAY_INDEX_BITMASK];
    }

    /**
     * Writes a single byte into memory, allocate more chunks if needed.
     * */
    void write(int address, byte value){
        int key = address >> 16;

        if(!internal.containsKey(key)){
            if(value == 0) return;
            byte[] array = new byte[BYTE_ARRAY_SIZE];
            array[address & ARRAY_INDEX_BITMASK] = value;
            internal.put(key, array);
        }else {
            internal.get(key)[address & ARRAY_INDEX_BITMASK] = value;
        }

    }

    /**
     * Reads a 32-bit word.
     * */
    int readWord(int address) {
        return ((read(address + 3) & 0xFF) << 24)
                | ((read(address + 2) & 0xFF) << 16)
                | ((read(address + 1) & 0xFF) << 8)
                | ((read(address) & 0xFF));
    }

    /**
     * Writes to a 32-bit address.
     * */
    void writeWord(int address, int word) {
        write(address,     (byte) (word & 0xFF));
        write(address + 1, (byte) ((word >> 8) & 0xFF));
        write(address + 2, (byte) ((word >> 16) & 0xFF));
        write(address + 3, (byte) ((word >> 24) & 0xFF));
    }

}

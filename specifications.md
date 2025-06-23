# Trivial ISA

*Trivial* is an ISA designed for educational purposes. Inspired by ANU's [QuAC](https://comp.anu.edu.au/courses/comp2300/resources/08-QuAC-ISA/)
ISA and ARM assembly, Trivial is designed to be more complex than QuAC. In this document, `bn` represents represents the 
n'th least significant bit of an arbitrary bit string. 

## Data Width

Trivial uses 32-bit registers, byte-addressable memory with 32-bit addresses along with 32-bit instructions.

## Register File


| Register | Usage           | ABI Role                    |
|----------|-----------------|-----------------------------|
| `r0`     | Temporary       | Caller-saved / Return Value |
| `r1`     | Temporary       | Caller-saved                |
| `r2`     | Temporary       | Caller-saved                |
| `r3`     | Temporary       | Caller-saved                |
| `r4`     | Temporary       | Caller-saved                |
| `r5`     | Temporary       | Caller-saved                |
| `r6`     | Temporary       | Caller-saved                |
| `r7`     | Temporary       | Caller-saved                |
| `r8`     | General Purpose | Callee-saved                |
| `r9`     | General Purpose | Callee-saved                |
| `r10`    | General Purpose | Callee-saved                |
| `r11`    | General Purpose | Callee-saved                |
| `fl`     | Flag Register   | N/A                         |
| `lr`     | Link Register   | Caller-saved                |
| `pc`     | Program Counter | N/A                         |
| `sp`     | Stack Pointer   | Callee-saved                |

### Flag Register

Least significant four bits represents the ALU flag, which are
- Negative (N), set to `b0`, if the previous ALU operation results in a negative result.
- Zero (Z), set to `b1`, if the previous ALU operation results in 0.
- Carry (C), set to `b2`, if the previous operation is an arithmetic operation and an unsigned carry-out occurs.
- Overflow (V), set to `b3` if the result overflowed the signed range. 

### Program Counter

The program counter is initially 0, and each sequential instruction executed increments the program counter by 4, unless 
a branch instruction occurs. 

### Stack Pointer

Trivial utilises a full-descending callstack starting from `0x00000000`, pushing a 32-bit value will then cause the 
stack pointer to move to `0xFFFFFFFC`.

### Link Register 

The link register holds the return address of a function call, initially set to 0. When a `CALL` instruction occurs, 
`lr` is pushed to the stack, and when `RET` is called, `lr` is popped from the stack which then sets `pc`. 

### Temporary Registers

Temporary registers are caller-saved registers where a function may modify these registers freely. 

### General Purpose Registers

General purpose registers are callee-saved registers where the initial values should be invariant under a function call. 
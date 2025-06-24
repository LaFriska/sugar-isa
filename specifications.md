# Sugar ISA

*Sugar* is a RISC-ISA designed for educational purposes. Inspired by ANU's [QuAC](https://comp.anu.edu.au/courses/comp2300/resources/08-QuAC-ISA/)
ISA and ARM assembly, Sugar is designed to be more complex than QuAC. In this document, `bn` represents the 
n'th least significant bit of an arbitrary bit string. 

## Data Width

Sugar uses 32-bit registers, byte-addressable memory with 32-bit addresses along with 32-bit instructions.

## Register File


| Register | Usage           | ABI Role                       |
|----------|-----------------|--------------------------------|
| `r0`     | Zero Register   | -                              |
| `r1`     | Temporary       | Caller-saved / Return Register |
| `r2`     | Temporary       | Caller-saved                   |
| `r3`     | Temporary       | Caller-saved                   |
| `r4`     | Temporary       | Caller-saved                   |
| `r5`     | Temporary       | Caller-saved                   |
| `r6`     | Temporary       | Caller-saved                   |
| `r7`     | General Purpose | Callee-saved                   |
| `r8`     | General Purpose | Callee-saved                   |
| `r9`     | General Purpose | Callee-saved                   |
| `r10`    | General Purpose | Callee-saved                   |
| `r11`    | General Purpose | Callee-saved                   |
| `fl`     | Flag Register   | N/A                            |
| `lr`     | Link Register   | Caller-saved                   |
| `pc`     | Program Counter | N/A                            |
| `sp`     | Stack Pointer   | Callee-saved                   |

### Zero Register

The zero register is a constant 0, writing to it does nothing, reading it will always result in 0. 

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

Sugar utilises a full-descending callstack starting from `0x00000000`, pushing a 32-bit value will then cause the 
stack pointer to move to `0xFFFFFFFC`.

### Link Register 

The link register holds the return address of a function call, initially set to 0. When a `CALL` instruction occurs, 
`lr` is pushed to the stack, and when `RET` is called, `lr` is popped from the stack which then sets `pc`.

### Temporary Registers

Temporary registers are caller-saved registers where a function may modify these registers freely. 

### General Purpose Registers

General purpose registers are callee-saved registers where the initial values should be invariant under a function call.

## Assembly Syntax

Unlike QuAC and ARM, Sugar is case-sensitive and uses a more modern syntax. Instructions are separated by `;`, and every instruction
is written in lower-case. Labels however, are written in upper-case. In this section, `immN` represents an
immediate value of `N` bits. `ra`, `rb` are arbitrary registers and `rd` represents the destination register.

### Immediates 

Immediate values can be written as hexadecimal, decimal, or binary in the following format:

- Hexadecimal: `0xABCD`
- Decimal: `1234`
- Binary: `0b0110111`

### Comments 

Following C-style syntax, use double-slash to denote a comment. For instance,

`r3 = r1 + r2; // this is a comment`. 

For longer comments, open with `/*` and end with `*/`.

> Example:
> ```
> /*
> This is a very long comment.
> With many lines.
> Above some nice Sugar assembly code.
> */
> r3 = r1 + R2; //An ADD instruction
> r1 = r3;   //A move instruction
> r3 = mem[0xAB];  //A memory read instruction
> ```

### Move

Sets a register to a value held by another register, or an immediate. 

Syntax: `rd = ra`, `rd = imm16`. 

> Example:
> ```
> r1 = r2;      //R-format
> r1 = 0xABCD;  //I-format
> ```

### Arithmetic Operations

There are five arithmetic operations.
1. add, `+`
2. subtract, `-`
3. multiply, `*`
4. signed division, `/`
5. modulo, `%`

Syntax:
``` 
rd = ra + rb;
rd = ra - rb;
rd = ra * rb;
rd = ra / rb;
rd = ra % rb;
```
The following syntactical sugar may also be used,

``` 
rd += rb;
rd -= rb;
rd *= rb;
rd /= rb;
rd %= rb;
```
which replaces `ra` with `rd`.

### Logical Operations 

There are 6 logical operations.

1. and, `&`
2. or, `|`
3. xor, `^`
4. not, `!`
5. left-shift, `<<`
6. right-shift, `>>`

Syntax:

``` 
rd = ra & rb;
rd = ra | rb;
rd = ra ^ rb;
rd = !ra;
rd = ra << rb;
rd = ra >> rb;
```

Similarly, the following syntactical sugar may also be used.

``` 
rd &= rb;
rd |= rb;
rd ^= rb;
!rd;
rd << rb;
rd >> rb;
```

Note that for syntactical sugar above, `rb` could also be replaced with `imm16`.

> Example:
> ```
> r1 = 16;
> r1 *= r1;
> r3 = 10;
> r1 = r3 + 5;
> r3 |= 0b1;
> ```

### Memory Addressing 

To represent a value held in memory without offsets, use `mem[ra]` or `mem[imm16]`. To represent
a value held in memory with an offset, use `mem[ra + rb]`, `mem[ra + imm16]`, or `mem[ra - imm16]`. 

Then, the memory read and memory write instructions are as follows. 

Memory Read: 

``` 
rd = mem[ra];
rd = mem[imm16];
rd = mem[ra + rb];
rd = mem[ra + imm16];
rd = mem[ra - imm16];
```
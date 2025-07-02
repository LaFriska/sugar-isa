# Sugar ISA

*Sugar* is a RISC-ISA designed for educational purposes. Inspired by ANU's [QuAC](https://comp.anu.edu.au/courses/comp2300/resources/08-QuAC-ISA/)
ISA and ARM assembly, Sugar is designed to be more complex than QuAC. In this document, `bn` represents the 
n'th least significant bit of an arbitrary bit string. 

## Data Width

Sugar uses 32-bit registers, byte-addressable memory with 32-bit addresses along with 32-bit instructions.

## Register File


| Register | Usage           | ABI Role                       | Binary Encoding |
|----------|-----------------|--------------------------------|-----------------|
| `r0`     | Zero Register   | -                              | `0000`          |
| `r1`     | Temporary       | Caller-saved / Return Register | `0001`          |
| `r2`     | Temporary       | Caller-saved                   | `0010`          |
| `r3`     | Temporary       | Caller-saved                   | `0011`          |
| `r4`     | Temporary       | Caller-saved                   | `0100`          |
| `r5`     | Temporary       | Caller-saved                   | `0101`          |
| `r6`     | Temporary       | Caller-saved                   | `0110`          |
| `r7`     | General Purpose | Callee-saved                   | `0111`          |
| `r8`     | General Purpose | Callee-saved                   | `1000`          |
| `r9`     | General Purpose | Callee-saved                   | `1001`          |
| `r10`    | General Purpose | Callee-saved                   | `1010`          |
| `r11`    | General Purpose | Callee-saved                   | `1011`          |
| `fl`     | Flag Register   | N/A                            | `1100`          |
| `lr`     | Link Register   | Caller-saved                   | `1101`          |
| `pc`     | Program Counter | N/A                            | `1110`          |
| `sp`     | Stack Pointer   | Callee-saved                   | `1111`          |


### Zero Register

The zero register is a constant 0, writing to it does nothing, reading it will always result in 0. 

### Flag Register

Least significant four bits represents the ALU flag, which are
- Negative (N), set to `b0`, if the previous ALU operation results in a negative result.
- Zero (Z), set to `b1`, if the previous ALU operation results in 0.
- Carry (C), set to `b2`, if the previous operation is an arithmetic operation and an unsigned carry-out occurs.
- Overflow (V), set to `b3` if the result overflowed the signed range. 
- Other bits are undefined and reserved for future updates. 

The flag register is set by a chained `flag;` after arithmetic or logical instructions.
More information can be found in the Sugar Assembly section of this document. 

### Program Counter

The program counter is initially 0, and each sequential instruction executed increments the program counter by 4, unless 
a branch instruction occurs. 

### Stack Pointer

Sugar utilises a full-descending callstack starting from `0x00000000`, pushing a 32-bit value will then cause the 
stack pointer to move to `0xFFFFFFFC`.

### Link Register 

The link register holds the return address of a function call, initially set to 0. When a `call` instruction occurs, 
`lr` is pushed to the stack, and when `return` is called, `lr` is popped from the stack which then sets `pc`.

### Temporary Registers

Temporary registers are caller-saved registers where a function may modify these registers freely. 

### General Purpose Registers

General purpose registers are callee-saved registers where the initial values should be invariant under a function call.

## Sugar Assembly

Unlike QuAC and ARM, Sugar is case-sensitive and uses a more modern syntax. Instructions are separated by `;`, and every instruction
is written in lower-case. Labels however, are written in upper-case. In this section, `immN` represents an
immediate value of `N` bits. `ra`, `rb` are arbitrary registers and `rd` represents the destination register.

### Immediates 

Immediate values can be written as hexadecimal, decimal, or binary in the following format:

- Hexadecimal: `0xABCD`
- Decimal: `1234`
- Binary: `0b0110111`

Sugar uses immediate values of three different widths, arithmetic, logical and memory operations uses an `imm16`, 
except the set and not instructions, which use `imm22`,
while stack instructions, branching and function calls use `imm26`. 

### Comments 

Following C-style syntax, use double-slash to denote a comment. For instance,

`r3 = r1 + r2; // this is a comment`.

### Set Instruction (Move)

Sets a register to a value held by another register, or an immediate. 

Syntax: `rd = ra`, `rd = imm22`. 

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
3. xor, `^`

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

For the not instruction, `ra` may be replaced with `imm22`.

For instance, `r1 = !0b1111010101110011000010;` is valid Sugar assembly, although an I-format not is typically redundant.

### Keeping the ALU Flags

By default, arithmetic and logical operations do not update the flag register (`fl`). In order to set `fl` by the resulting
ALUFLAGs of an operation, chain a `flag;` instruction to the instruction like so.

> Example
> ```
> r1 = r2 + r3 -> flag; 
> ```
Note that the not and set instruction does not allow a `flag;` chain. 

### Memory Addressing 

**IMPORTANT**: In this subsection, `ra - imm16` is a notational sugar for adding a negative 16-bit immediate. Hence,
`r1 - -1` would be invalid, while `r1 - 3` would be valid, which is semantically equivalent to `r1 + -3`. Note that in reality,
offsets only use ALU addition. 

To represent a value held in memory without offsets, use `[ra]` or `[imm16]`. To represent
a value held in memory with an offset, use `[ra + rb]`, `[ra + imm16]`, or `[ra - imm16]`. 

Then, the memory read and memory write instructions are as follows. 

Memory Read: 

``` 
rd = [ra];
rd = [imm16];
rd = [ra + rb];
rd = [ra + imm16];
rd = [ra - imm16];
```

Memory Write:

``` 
[rd] = ra; 
[imm16] = ra;
[rd + rb] = ra;
[rd + imm16] = ra;
[rd - imm16] = ra;
```

Pre/Post-offset memory operations are also supported with the following syntax.

Pre-offset Memory Read: 

``` 
ra += rb -> rd = [ra];
ra += imm16 -> rd = [ra];
ra -= imm16 -> rd = [ra];
```

Pre-offset Memory Write:

``` 
rd += rb -> [rd] = ra;
rd += imm16 -> [rd] = ra;
rd -= imm16 -> [rd] = ra;
```

Post-offset Memory Read:

``` 
rd = [ra] -> ra += rb;
rd = [ra] -> ra += imm16;
rd = [ra] -> ra -= imm16;
```

Post-offset Memory Write:

``` 
[rd] = ra -> ra += rb;
[rd] = ra -> ra += imm16;
[rd] = ra -> ra -= imm16;
```

Note that the notation `[rd] = ra -> ra = ra + rb;` is also acceptable. Since computing offsets also require an ALU 
operation, Sugar ISA allows setting the flag register during a memory operation by chaining the `flag;` instruction.

> Example
> ```
> [r1] = r2 -> r2 += r3 -> flag;
> r2 = [r1 + 1] -> flag; 
> ```

### Stack Operations 

Syntax: 

``` 
push rd; //Pushes rd onto the stack
pop rd;  //Pops stack into rd.
```

A notational sugar for pushing and popping multiple registers is as follows.

``` 
push r1, r2, r3;
pop r1, r2, r3;
```
This is equivalent to 
``` 
push r1;
push r2;
push r3;
pop r1;
pop r2;
pop r3;
```

Similarly, stack operations support immediate values,

``` 
push imm16;
pop imm16; 
```

with similar notational sugar for pushing and popping multiple values from the stack.

``` 
push imm16, imm16, imm16, ...;
pop imm16, imm16, imm16, ...;
```


### Branching

Labels are written with at least one capital letter, 0 or more digits or underscores, and must end in `:`. Labels may denote either functions, or instructions to branch 
to. Branching commands are executed using instructions `goto rd;`, `goto imm26;`, or `goto LABEL;`.

> Example
> ``` 
> r3 = 0xFF;
> FILL_WITH_3_TIMES_5:
>   r1 = 3;
>   r2 = 5;
>   r2 *= r1;
>   [r3] = r2 -> r3 += 1;
>   goto FILL_WITH_3_TIMES_5;
> ```

Conditional branching can be used by using the following instructions.

1. `goton` - branches if `N` is set in the flag register.
2. `gotoz` - branches if `Z` is set in the flag register.
3. `gotoc` - branches if `C` is set in the flag register.
4. `gotov` - branches if `V` is set in the flag register.

> Example
> ``` 
> r3 = 0xFF;
> FILL_WITH_3_TIMES_5:
>   r1 = 3;
>   r2 = 5;
>   r2 *= r1;
>   [r3] = r2 -> r3 += 1;
>   r4 = 0xFFFF;
>   r0 = r3 - r4 -> flag;       // Comparison to set the flag register.
>   goton FILL_WITH_3_TIMES_5;  // Executes the branch instruction if r3 < r4. 
> ```

Recall that `r0` is an immutable constant 0. Hence, it is always used for comparisons. For this reason, `compare ra, rb;`
is used as a pseudo-instruction for `r0 = ra - rb -> flag;`. The flags can then be interpreted
as follows. 
1. Negative (N): `ra < rb`.
2. Zero (Z): `ra = rb`. 
3. Overflow (V): Signed overflow, which can be interpreted as a warning that the negative and zero flag may be inaccurately interpreted. 

### Function Calls 

A function call achieves the same result, but saves the instruction after the call to the link register `lr`. 

Syntax:
``` 
call LABEL;
call imm26; 
call rd;
```

Of course, we can also use conditional execution for `call`, and chained `goto` calls. 

> Example
> ```
> callv FUNCTION;
> ```

Then, returning from a function call is just a `goto` to the link register. A notational sugar is defined for 
simplicity.

``` 
goto lr;
return;        //These instructions do the same thing.
```

If multiple function calls are chained in one another, it is often necessary to push the link register onto the stack,
and pop it after the function call. 

**Note** - Chaining a `flag;` instruction is not supported for `goto` or `call` instructions. 

### Other Notes

**Whitespace Format** - Sugar follows C-style flexible whitespace, where whitespaces may separate tokens but are
only required where needed to avoid ambiguity. This can be formalised by the following rule:
> Rule: *At least one whitespace is required between adjacent alphanumerical tokens, possibly with underscores.*
> 
> Example:
> ```
> //These instructions are valid.
> r1+=r2 ;
> r1 += r2;
> goto MAIN_FUNCTION;
> r1 = r3 ^ r4->flag;
> push r3;push r2;
> goton 0x1234;
> 
> //These instructions are invalid.
> pushr1;
> gotoMAIN_FUNCTION;
> goton0x1234
> ```
> Note that immediates count as alphanumerical tokens. 

Despite the loose whitespace rules, it is still *seriously recommended* to insert consistent whitespaces
between tokens, and write a single instruction per line of code. For instance, this is bad.

``` 
r1 =r2+r3;r3= [r5+1];;
```

This however, is much cleaner.

``` 
r1 = r2 + r3;
r3 = [r5 + 1];
;
```

**Null instruction** -
An instruction that does nothing and wastes a clock cycle can be written as just an empty instruction ended with a semicolon.
> Example
> ``` 
> [r1] = r2;
> ;             //Does nothing for 3 cycles
> ;
> ;
> r3 = [r1];
> ```

These are pseudo-instructions for `r0 = 0;`. Note that adding extra semicolons at the end of an instruction also counts as adding extra null instructions.
For example, `[r1] = r2;;` is actually 2 instructions. 

## Instruction Encoding

Below is the full instruction set encoding of the Sugar ISA. Any other opcodes are undefined, or reserved for future updates. 

![instructionset.jpeg](items/instructionset.jpeg)
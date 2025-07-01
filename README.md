# Sugar ISA 

Sugar is an educational ISA designed for the simplicity of its assembly code. As of today, assembly
syntax tends to follow the `mnemonic, arguments` syntax. For instance, the syntax to add two values 
in ARM is `ADD rd, ra, rb`. This syntax is simple, and perfectly represents the underlying instruction given
to the microarchitecture. However, this syntax may often be unclear and difficult to read to beginners who
are not particularly familiar with the microarchitecture and the way instructions are encoded in binary.
Sugar ISA is designed to simplify assembly to follow C-style syntax. For instance, the `ADD` instruction
is simply written as `rd = ra + rb;`, and memory write is written as `[rd] = ra;`. For the full Sugar
specification, see the [specifications](specifications.md).

## Emulator
 
This project achieves *two* goals: fully specify Sugar using markdowns, and emulate 
Sugar assembly by implementing an Emulator library in Java. The emulator follows a 
three-stage protocol.
1. **Tokenise** - Sugar assembly as a string is processed into internal representation of tokens.
2. **Parse** - The list of tokens are parsed and converted into internal representation of instructions.
3. **Execute** - The list of instruction can then be executed sequentially in a simulated microarchitecture in Java. 

Although not implemented in this project, a frontend interface may be implemented as a future project. 


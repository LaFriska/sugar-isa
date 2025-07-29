# Sugar ISA, Parser, and Emulator

This project achieves two goals. 
1. Formally design and specify a distinctive RISC-ISA, including assembly syntax, for educational purposes.
2. Emulate assembly parser and program execution using Java.

## Personal Agenda

This project was implemented for my CV, to demonstrate my understanding of computer architecture at a very detailed level,
and to also showcase my skills in software engineering, data structures and object-oriented programming. Most of these
concepts were taught to me from courses at the Australian National University, such as COMP2300 and COMP2100.

## ISA

Sugar Instruction Set Architecture (ISA) is designed for the simplicity of its assembly code, and for education purposes,
as it does not have any real-world practical uses. Sugar is a RISC ISA, inspired by ARM and ANU's QuAC ISAs. The assembly
is purposely designed to use C-style syntax, for uniqueness and user-friendliness. For the full specification, see the [specifications](specifications.md).

See below for an example of Sugar assembly code that calculates the factorial of a number.

``` 
MAIN:
    push lr;

    r1 = 10;
    call FAC;
    goto END;

// r1 holds n, returns n! through r1.
FAC:
    push lr;
    r2 = 2;
    compare r1, r2;
    goton BASE_CASE;

    push r1;
    r1 -= 1;
    call FAC;
    pop r2;
    r1 *= r2;

    pop lr;
    return;

BASE_CASE:
    r1 = 1;
    pop lr;
    return;

END:
    pop lr;
```

## User Guide 

To use the emulator, first create a `SugarExecutor` instance.
```java
import xyz.haroldgao.sugarisa.execute.SugarExecutor;

public class Example{
  
    public static void main(String[] args){
        
        String assembly = "r1 = r2 + 50;";
        Sugar executor = SugarExecutor.load(assembly);  
          
    }
  
}
```

Then, with the instance, use `SugarExecutor#next()` to execute a single
instruction, or `SugarExecutor#execute();`, which executes all instructions until
it reads a termination instruction. When any of these methods are called, 
an instance of `ArchitecturalState` is returned, which contains a maximum 4GB of emulated memory,
along with a register file. `ArchitecturalState#read(int)` can be called
to read from memory, while `ArchitecturalState#read(Register)` can be used to 
read a register. Similarly, `ArchitecturalState#write(int, int)`, and `ArchitecturalState#write(Register, int)`
can be used to write to memory, or the register file.

```java
import xyz.haroldgao.sugarisa.execute.ArchitecturalState;
import xyz.haroldgao.sugarisa.execute.Register;
import xyz.haroldgao.sugarisa.execute.SugarExecutor;

public class Example {

  public static void main(String[] args) {

    String assembly = "r1 = r2 + 50;";
    Sugar executor = SugarExecutor.load(assembly);

    ArchitecturalState state = executor.execute();

    //Should print 50.
    System.out.println(state.read(Register.R1));

  }

}
```



## Parser

This project includes a parser which is capable of converting Sugar assembly into a list of internal representation
of instructions. This is achieved through the following pipeline. 

For this section, the following example will be used to explain this process.

``` 
FUNCTION: //Comment
    r2 = r3;
    goto END;
END:
    ;
```

1. The [Tokeniser](src/main/java/xyz/haroldgao/sugarisa/tokeniser/Tokeniser.java) converts the code into a list of tokens.
The assembly string in our example will be converted to the 
following tokens.

```
tokens: [
    {label, "FUNCTION"},
    {colon, ":"},
    {comment, "Comment"}, 
    {keyword, "r2"}, 
    {equals, "="}, 
    {keyword, "r3"}, 
    {terminator, ";"},
    {keyword, "goto"}, 
    {label, "END"},
    {terminator, ";"}, 
    {label, "END"},
    {colon, ":"}, 
    {terminator, ";"}
]
```

The tokeniser takes care of whitespaces and invalid combinations of characters.
2. A [Linker](src/main/java/xyz/haroldgao/sugarisa/parser/Linker.java) then removes all comment tokens, splits the list
based on the terminator token, removes and links each label to the program address of the instruction it is pointing to.
Hence, we have the following results.

```
instructions: [
    [
        {keyword, "r2"},
        {equals, "="}, 
        {keyword, "r3"},
        {terminator, ";"}
    ],
    [
        {keyword, "goto"}, 
        {label, "END"}, 
        {terminator, ";"}
    ],
    [
        {terminator, ";"}
    ]
]

labelMap:
    "FUNCTION" -> 0
    "END" -> 12
```

3. For each set of tokens for a specific instruction, The [Parser](src/main/java/xyz/haroldgao/sugarisa/parser/Parser.java) then takes
the map produced by the linker and the tokens as input, and parses the tokens based on a single instance of 
a [decision-parse-tree](src/main/java/xyz/haroldgao/sugarisa/parser/ParseTree.java). This data structure
recurses through the list of tokens and its children, while potentially saving data to a [ParseState](src/main/java/xyz/haroldgao/sugarisa/parser/ParseState.java).
Functional interfaces also generalises the process of error handling, and returning instructions. 
See [SugarParseTree](src/main/java/xyz/haroldgao/sugarisa/parser/SugarParseTree.java) for the singleton
construction of the tree specifically for Sugar. Hence, our example above will be converted to the following list of instructions.

``` 
instructions: [
    {set r2, r3},
    {goto END},
    {set r0, r0}
]
```
(Note that the instruction represented by a single semicolon is specifically `{set r0, r0}`, which does nothing.)

## Program Execution

With the same example as above, but transformed to a set of instructions, 
this section describes how program execution is emulated. 
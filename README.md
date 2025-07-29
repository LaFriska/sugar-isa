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
    "END" ->
```



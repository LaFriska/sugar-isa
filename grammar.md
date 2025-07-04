# Grammar

We define the context-free grammar (CFG) and its production rules of sugar assembly in this document, 
in terms of the tokens defined [here](tokens.md). 

## Notes

1. `<string>` is a non-terminal symbol.
2. `TOKEN` is a terminal symbol resulting in a specific token.
3. This CFG ignores whitespace rules, see the [specifications](specifications.md) document for whitespace rules of sugar assembly.
4. The `COMMENT` token is ignored for simplicity.
5. The specific lengths of an immediate is ignored for simplicity, while in reality an immediate value is checked for its length before storing it in an Instruction instance.
6. The starting symbol is `<start>`.
7. `ε` denotes the empty string.

We will also split the `KEYWORD` token into multiple sub-tokens.
1. `REG` denotes all the registers: `r1`, `sp`, etc. 

## Production Rules

### Utility Definitions 

``` 
<reg_or_imm> := REG | <imm>
<imm> := IMM_HEX | IMM_DEC | IMM_BIN
```

### Overview

``` 
<start> := <part> TERM <start> | ε
<part> := LABEL COLON <instruction> | <instruction>
<instruction> := <duo> | <solo> | <simple>
```

### Arithmetic Duo-Instructions

Duo-Instructions are those that use `rd, ra, rb/imm16`.

``` 
<duo> := REG EQ REG <duo_op> <reg_or_imm> | REG <duo_op_eq> <reg_or_imm>
<duo_op> := ADD | SUB | MUL | DIV | MOD | OR | AND | XOR | LEFT_SHIFT | RIGHT_SHIFT  
<duo_op_eq> := ADD_EQ | SUB_EQ | MUL_EQ | DIV_EQ | MOD_EQ | AND_EQ | OR_EQ | XOR_EQ
```

### Memory Duo-Instructions

### Solo-Instructions

These are instructions that use `rd, ra/imm22`. 
``` 
<solo> := <set> | <not> 
<set> := REG EQ <reg_or_imm>
<not> := REG EQ NOT <reg_or_imm> | NOT REG
```

### Simple Instructions

These are instructions that takes a single `rd/imm26` as input. 

``` 
<simple>
```
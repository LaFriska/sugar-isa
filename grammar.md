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
2. `GOTO`, `GOTON` etc... denotes specific `KEYWORD` values. 

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
<duo> := <duo_alu> <flag> | <duo_mem> <flag>
<duo_alu> := REG EQ REG <duo_op> <reg_or_imm> | REG <duo_op_eq> <reg_or_imm>
<duo_op> := ADD 
          | SUB 
          | MUL 
          | DIV 
          | MOD 
          | OR
          | AND 
          | XOR 
          | LEFT_SHIFT 
          | RIGHT_SHIFT  
<duo_op_eq> := ADD_EQ 
             | SUB_EQ 
             | MUL_EQ 
             | DIV_EQ 
             | MOD_EQ 
             | AND_EQ 
             | OR_EQ 
             | XOR_EQ
<flag> := CHAIN FLAG
```

### Memory Duo-Instructions

``` 
<duo_mem> := <write> 
           | <read> 
           | <pre_write> 
           | <pre_read> 
           | <post_write> 
           | <post_read>

<write> := <mem_value> EQ <reg_or_imm>
<read> := REG EQ <mem_value>

<pre_write> := <pre_offset> CHAIN <write>
<pre_read> := <pre_offset> CHAIN <read>
<post_write> := <write> CHAIN <post_offset>
<post_read> := <read> CHAIN <post_offset>

<pre_offset> := REG ADD_EQ REG 
              | REG ADD_EQ <imm>
              | REG SUB_EQ <imm>

<post_offset> := REG ADD_EQ REG 
               | REG ADD_EQ <imm>
               | REG SUB_EQ <imm>

<mem_value> := LBRAC <address> RBRAC
<address> := REG ADD <reg_or_imm> 
           | <reg_or_imm>
```

### Solo-Instructions

These are instructions that use `rd, ra/imm22`. 
``` 
<solo> := <set> | <not> 
<set> := REG EQ <reg_or_imm>
<not> := REG EQ NOT <reg_or_imm> | NOT REG
```

### Simple Instructions

``` 
<simple> := <branch> | <stack>
<branch> := <branch_mnemonic> <reg_imm> | LABEL
<stack> := <stack_mnemonic> <multiple_values>
<multiple_values> := <reg_or_imm> | <reg_or_imm> COMMA <multiple_registers>
<branch_mnemonic> := GOTO 
                   | GOTON
                   | GOTOZ 
                   | GOTOC 
                   | GOTOV 
                   | CALL 
                   | CALLN 
                   | CALLZ 
                   | CALLC 
                   | CALLV
<stack_mnemonic> := PUSH | POP
```


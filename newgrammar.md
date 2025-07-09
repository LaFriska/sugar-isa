# Context-Free Grammar 

For sake of simplicity, immediate value size is disregarded in this
document. 

## Basic Definitions 

Let `<reg>` be a keyword denoting any registers, and let `<imm>`
denote any immediate value.

``` 
<value> = <reg> | <imm>
<branch_mnemonic> = GOTO 
                  | GOTON
                  | GOTOZ 
                  | GOTOC 
                  | GOTOV 
                  | CALL 
                  | CALLN 
                  | CALLZ 
                  | CALLC 
                  | CALLV
```

## Full Production Rule

``` 
<start> = <p1>
<p1> = <reg> <p2_reg>
     | <branch_mnemonic> <p2_branch>
     | LBRAC <p2_load>
     | TERM
     | NOT <value> TERM
```

``` 
<p2_reg> = 
```
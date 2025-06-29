# Tokens for Sugar Assembly

| Token         | Specification                                                 |
|---------------|---------------------------------------------------------------|
| `LABEL`       | At least one capital letter, 0 or more digits or underscores. |
| `IMM_HEX`     | `0x` followed by one or more case-insensitive hex characters. |
| `IMM_DEC`     | One or more decimal digits.                                   |
| `IMM_BIN`     | `0b` followed by one or more binary digits.                   |
| `COMMENT`     | `//` followed by any series of characters without line break. |
| `EQ`          | `=`                                                           |
| `TERM`        | `;`                                                           |
| `COLON`       | `:`                                                           |
| `ADD`         | `+`                                                           |
| `SUB`         | `-`                                                           |
| `MUL`         | `*`                                                           |
| `DIV`         | `/`                                                           |
| `MOD`         | `%`                                                           |
| `AND`         | `&`                                                           |
| `OR`          | `\|`                                                          |
| `XOR`         | `^`                                                           |
| `NOT`         | `!`                                                           |
| `LEFT_SHIFT`  | `<<`                                                          |
| `RIGHT_SHIFT` | `>>`                                                          |
| `ADD_EQ`      | `+=`                                                          |
| `SUB_EQ`      | `-=`                                                          |
| `MUL_EQ`      | `*=`                                                          |
| `DIV_EQ`      | `/=`                                                          |
| `MOD_EQ`      | `%=`                                                          |
| `AND_EQ`      | `&=`                                                          |
| `OR_EQ`       | `\|=`                                                         |
| `XOR_EQ`      | `^=`                                                          |
| `CHAIN`       | `->`                                                          |
| `COMMA`       | `,`                                                           |
| `LBRAC`       | `[`                                                           |
| `RBRAC`       | `]`                                                           |
| `KEYWORD`     | See next section.                                             |

## Keywords

Keywords are defined as any strictly lowercase alphanumerical word with at least one alphabetical character. 
The following are keywords of Sugar assembly. 

### Registers 

- `r0`
- `r1`
- `r2`
- `r3`
- `r4`
- `r5`
- `r6`
- `r7`
- `r8`
- `r9`
- `r10`
- `r11`
- `fl`
- `lr`
- `pc`
- `sp`

### Branching 

- `goto`
- `goton`
- `gotoz`
- `gotoc`
- `gotov`
- `call`
- `calln`
- `callz`
- `callc`
- `callv`
- `return`

### Conditional Execution

- `flag`
- `compare`
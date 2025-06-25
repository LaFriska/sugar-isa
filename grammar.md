# Context-Free Grammar

This file specifies the context-free grammar for the Sugar Assembly language, as specified
in the [specifications](specifications.md). 

In this document, `<symbol>^N` means `<symbol>` repeated `N` times, `<symbol>^<=N` means `<symbol>`
repeated at least once and less or equal to `N` times. These additional notations are *regular*, which means they are
a subset of context-free languages, which does not make our production rules more powerful. We also let
`<dec_N>` denote any decimal digit which may be represented using `N`-bit *2's complement*.

## Basic Elements

Some basic elements are defined as follows. Let `<ws>` be defined as a single whitespace, which are spaces, horizontal tabs, 
line feeds, carriage returns, form feeds and vertical tabs. 

``` 
<whitespaces>   := <ws> | <whitespaces>
<bin_char>      := 0 | 1
<digit_char>    := 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 | <bin_char>
<hex_char>      := a | A | b | B | c | C | d | D | e | E | f | F | <digit_char>
<hex_16>        := 0x <hex_char>^<=4
<bin_16>        := 0b <bin_char>^<=16 
<imm_16>        := <hex_16> | <bin_16> | <dec_16>
<hex_26>        := 0x <hex_char>^<=6
<bin_26>        := 0b <bin_char>^<=26 
<imm_26>        := <hex_26> | <bin_26> | <dec_26>
<reg>           := r0 | r1 | r2 | r3 | r4 | r5 | r6 | r7 | r8 | r10 | r11 | fl | lr | pc | sp
```

## Instructions 

Let the starting symbol be `<sugar_assembly>`. 
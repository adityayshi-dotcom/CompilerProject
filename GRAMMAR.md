# BanglaCompiler BNF Grammar

This grammar describes the minimal language implemented by the project.

```bnf
<program> ::= <statement-list>

<statement-list> ::= <statement> <statement-list>
                   | ε

<statement> ::= <variable-declaration>
              | <assignment>
              | <if-statement>
              | <while-statement>

<variable-declaration> ::= <type> IDENTIFIER "=" <expression> ";"

<assignment> ::= IDENTIFIER "=" <expression> ";"

<type> ::= "সংখ্যা"
         | "লেখা"

<if-statement> ::= "যদি" "(" <expression> ")" <block> <optional-else>

<optional-else> ::= "নাহলে" <block>
                  | ε

<while-statement> ::= "যতক্ষণ" "(" <expression> ")" <block>

<block> ::= "{" <statement-list> "}"

<expression> ::= <comparison>

<comparison> ::= <addition> <comparison-tail>

<comparison-tail> ::= <comparison-operator> <addition> <comparison-tail>
                    | ε

<comparison-operator> ::= "=="
                        | "!="
                        | "<"
                        | "<="
                        | ">"
                        | ">="

<addition> ::= <multiplication> <addition-tail>

<addition-tail> ::= "+" <multiplication> <addition-tail>
                  | "-" <multiplication> <addition-tail>
                  | ε

<multiplication> ::= <primary> <multiplication-tail>

<multiplication-tail> ::= "*" <primary> <multiplication-tail>
                        | "/" <primary> <multiplication-tail>
                        | ε

<primary> ::= NUMBER
            | STRING
            | IDENTIFIER
            | "(" <expression> ")"
```

## Reserved Bangla Keywords

| Keyword | Meaning |
|---|---|
| `সংখ্যা` | integer type |
| `লেখা` | string type |
| `যদি` | if |
| `নাহলে` | else |
| `যতক্ষণ` | while |

## Operator Precedence

Highest to lowest:

1. Parenthesized expressions
2. `*` and `/`
3. `+` and `-`
4. `==`, `!=`, `<`, `<=`, `>`, `>=`

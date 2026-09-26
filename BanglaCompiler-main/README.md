# BanglaCompiler

BanglaCompiler is a simple educational compiler for a small Bangla-based programming language.

The compiler is implemented in Java and demonstrates the main phases of compilation:

```text
Bangla Source Code
        ↓
      Lexer
        ↓
      Tokens
        ↓
      Parser
        ↓
       AST
        ↓
 Semantic / Type Checker
        ↓
 Java Code Generator
        ↓
 Generated Java Program
```

## Language Features

The language intentionally contains only the features required for this project.

### Bangla Keywords

| Bangla Keyword | Meaning |
|---|---|
| `সংখ্যা` | Integer datatype |
| `লেখা` | String datatype |
| `যদি` | If |
| `নাহলে` | Else |
| `যতক্ষণ` | While |

### Supported Features

- Integer variables
- String variables
- Bangla identifiers
- English identifiers
- Variable declarations
- Variable assignments
- Integer literals
- String literals
- Arithmetic operators: `+ - * /`
- Comparison operators: `== != < <= > >=`
- Parentheses
- Blocks using `{ }`
- If/else statements
- While loops
- Operator precedence
- Lexical error detection
- Syntax error detection and basic recovery
- Semantic/type checking
- Java code generation

## Example Bangla Program

```text
সংখ্যা বয়স = 20;
সংখ্যা ফল = বয়স + 5 * 2;
লেখা অবস্থা = "শিশু";

যদি (বয়স >= 18) {
    অবস্থা = "প্রাপ্তবয়স্ক";
}
নাহলে {
    অবস্থা = "শিশু";
}

যতক্ষণ (বয়স < 25) {
    বয়স = বয়স + 1;
}
```

## Compiler Phases

### 1. Lexical Analysis

The lexer scans the Bangla source code character by character and converts it into tokens.

Example:

```text
সংখ্যা বয়স = 20;
```

produces tokens such as:

```text
INT
IDENTIFIER
ASSIGN
NUMBER
SEMICOLON
EOF
```

The lexer also tracks line and column positions.

### 2. Parser and AST

The parser receives the token list and checks whether the program follows the language grammar.

It constructs an Abstract Syntax Tree (AST) for:

- variable declarations
- assignments
- binary expressions
- if/else statements
- while statements
- blocks

The parser implements operator precedence, so:

```text
10 + 5 * 2
```

is interpreted as:

```text
10 + (5 * 2)
```

### 3. Semantic Analysis

The type checker maintains information about declared variables and detects semantic errors.

Examples detected:

```text
সংখ্যা বয়স = "রহিম";
```

Type mismatch:

```text
Cannot assign STRING to INT variable
```

Undeclared variables are also detected.

### 4. Java Code Generation

After successful lexical, syntactic, and semantic analysis, the compiler generates Java source code.

For example:

```text
সংখ্যা বয়স = 20;
```

becomes:

```java
int বয়স = 20;
```

Bangla control-flow statements are converted as follows:

```text
যদি      → if
নাহলে    → else
যতক্ষণ   → while
```

## Generated Java Example

The sample Bangla program generates Java similar to:

```java
public class GeneratedProgram {
    public static void main(String[] args) {
        int বয়স = 20;
        int ফল = (বয়স + (5 * 2));
        String অবস্থা = "শিশু";

        if ((বয়স >= 18)) {
            অবস্থা = "প্রাপ্তবয়স্ক";
        }
        else {
            অবস্থা = "শিশু";
        }

        while ((বয়স < 25)) {
            বয়স = (বয়স + 1);
        }
    }
}
```

## Error Handling

The compiler demonstrates errors from multiple compiler phases.

### Lexical Error

Example:

```text
সংখ্যা বয়স = 20 @ 5;
```

Output:

```text
Lexical error at line 1, column 17:
Unexpected character '@'
```

### Syntax Error

Example:

```text
সংখ্যা বয়স = ;
```

Output:

```text
Syntax error at line 1, column 14:
Expected expression.
```

### Semantic Error

Example:

```text
সংখ্যা বয়স = "ভুল";
```

Output:

```text
Semantic error:
Cannot assign STRING to INT variable 'বয়স'.
```

## Project Structure

```text
BanglaCompiler/
│
├── src/
│   ├── lexer/
│   ├── ast/
│   ├── parser/
│   ├── semantic/
│   ├── codegen/
│   ├── Main.java
│   └── BanglaCompiler.java
│
├── tests/
│   ├── LexerTest.java
│   ├── ParserTest.java
│   ├── TypeCheckerTest.java
│   └── JavaGeneratorTest.java
│
├── examples/
│   ├── full_program.bng
│   ├── valid_program.bng
│   ├── invalid_program.bng
│   ├── lexical_error.bng
│   ├── syntax_error.bng
│   └── type_error.bng
│
├── GRAMMAR.md
├── compile_final.bat
├── test_errors.bat
├── .gitignore
└── README.md
```

## Requirements

- Java 17 or newer
- UTF-8 support

The project is compiled using Java 17 compatibility.

## Build and Run

From the project root on Windows:

```cmd
compile_final.bat
```

This performs the complete compiler test:

```text
Bangla Source
→ Lexer
→ Parser
→ AST
→ Type Checker
→ Java Generator
→ Generated Java
→ javac
→ Execution
```

Successful output ends with:

```text
COMPLETE COMPILER TEST PASSED
```

## Test Error Handling

Run:

```cmd
test_errors.bat
```

This demonstrates:

- lexical errors
- syntax errors
- semantic/type errors

## Individual Tests

The project also contains tests for:

```text
Lexer
Parser
Operator precedence
Syntax recovery
Semantic analysis
Type checking
Java generation
```

## Grammar

The complete BNF grammar is available in:

```text
GRAMMAR.md
```

## Implementation Language

The compiler is implemented in Java.

The generated target language is also Java.

## Current Status

```text
Lexer                   Complete
Parser                  Complete
AST                     Complete
Semantic Analysis       Complete
Java Code Generation    Complete
Error Handling          Complete
End-to-End Compilation  Complete
```
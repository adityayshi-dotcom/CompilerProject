package lexer;

public enum TokenType {

    // Data types
    INT,
    STRING_TYPE,

    // Control flow
    IF,
    ELSE,
    WHILE,

    // Identifiers and literals
    IDENTIFIER,
    NUMBER,
    STRING,

    // Arithmetic operators
    PLUS,
    MINUS,
    STAR,
    SLASH,

    // Assignment
    ASSIGN,

    // Comparison operators
    EQUAL_EQUAL,
    NOT_EQUAL,
    LESS,
    LESS_EQUAL,
    GREATER,
    GREATER_EQUAL,

    // Brackets and symbols
    LEFT_PAREN,
    RIGHT_PAREN,
    LEFT_BRACE,
    RIGHT_BRACE,
    SEMICOLON,

    // Special tokens
    EOF,
    ERROR
}
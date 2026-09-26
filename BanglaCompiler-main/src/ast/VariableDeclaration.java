package ast;

import lexer.TokenType;

public class VariableDeclaration extends Statement {

    private final TokenType type;
    private final String name;
    private final Expression initializer;

    public VariableDeclaration(TokenType type, String name, Expression initializer) {
        this.type = type;
        this.name = name;
        this.initializer = initializer;
    }

    public TokenType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public Expression getInitializer() {
        return initializer;
    }

    @Override
    public String toString() {
        return "VarDecl(" + type + " " + name + " = " + initializer + ")";
    }
}

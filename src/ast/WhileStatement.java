package ast;

public class WhileStatement extends Statement {

    private final Expression condition;
    private final BlockStatement body;

    public WhileStatement(Expression condition, BlockStatement body) {
        this.condition = condition;
        this.body = body;
    }

    public Expression getCondition() {
        return condition;
    }

    public BlockStatement getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "While(" + condition + ", body=" + body + ")";
    }
}

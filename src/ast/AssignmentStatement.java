package ast;

public class AssignmentStatement extends Statement {

    private final String name;
    private final Expression value;

    public AssignmentStatement(String name, Expression value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Assign(" + name + " = " + value + ")";
    }
}

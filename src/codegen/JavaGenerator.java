package codegen;

import ast.*;
import lexer.TokenType;

import java.util.List;

public class JavaGenerator {

    private int indentLevel = 0;

    public String generateProgram(
            List<Statement> statements,
            String className) {

        StringBuilder out = new StringBuilder();

        out.append("public class ")
                .append(className)
                .append(" {\n");

        indentLevel++;
        appendIndent(out);
        out.append("public static void main(String[] args) {\n");

        indentLevel++;

        for (Statement statement : statements) {
            generateStatement(statement, out);
        }

        indentLevel--;
        appendIndent(out);
        out.append("}\n");

        indentLevel--;
        out.append("}\n");

        return out.toString();
    }

    private void generateStatement(
            Statement statement,
            StringBuilder out) {

        if (statement instanceof VariableDeclaration declaration) {

            appendIndent(out);

            out.append(javaType(declaration.getType()))
                    .append(" ")
                    .append(declaration.getName())
                    .append(" = ")
                    .append(generateExpression(
                            declaration.getInitializer()))
                    .append(";\n");

            return;
        }

        if (statement instanceof AssignmentStatement assignment) {

            appendIndent(out);

            out.append(assignment.getName())
                    .append(" = ")
                    .append(generateExpression(
                            assignment.getValue()))
                    .append(";\n");

            return;
        }

        if (statement instanceof IfStatement ifStatement) {

            appendIndent(out);

            out.append("if (")
                    .append(generateExpression(
                            ifStatement.getCondition()))
                    .append(") ");

            generateBlock(
                    ifStatement.getThenBranch(),
                    out
            );

            if (ifStatement.getElseBranch() != null) {

                appendIndent(out);
                out.append("else ");

                generateBlock(
                        ifStatement.getElseBranch(),
                        out
                );
            }

            return;
        }

        if (statement instanceof WhileStatement whileStatement) {

            appendIndent(out);

            out.append("while (")
                    .append(generateExpression(
                            whileStatement.getCondition()))
                    .append(") ");

            generateBlock(
                    whileStatement.getBody(),
                    out
            );

            return;
        }

        if (statement instanceof BlockStatement block) {
            generateBlock(block, out);
            return;
        }

        throw new IllegalArgumentException(
                "Unsupported statement: "
                        + statement.getClass().getSimpleName()
        );
    }

    private void generateBlock(
            BlockStatement block,
            StringBuilder out) {

        out.append("{\n");

        indentLevel++;

        for (Statement statement : block.getStatements()) {
            generateStatement(statement, out);
        }

        indentLevel--;

        appendIndent(out);
        out.append("}\n");
    }

    private String generateExpression(Expression expression) {

        if (expression instanceof LiteralExpression literal) {

            Object value = literal.getValue();

            if (value instanceof String text) {
                return "\""
                        + escapeJavaString(text)
                        + "\"";
            }

            return String.valueOf(value);
        }

        if (expression instanceof VariableExpression variable) {
            return variable.getName();
        }

        if (expression instanceof BinaryExpression binary) {

            return "("
                    + generateExpression(binary.getLeft())
                    + " "
                    + binary.getOperator().getLexeme()
                    + " "
                    + generateExpression(binary.getRight())
                    + ")";
        }

        throw new IllegalArgumentException(
                "Unsupported expression: "
                        + expression.getClass().getSimpleName()
        );
    }

    private String javaType(TokenType type) {

        if (type == TokenType.INT) {
            return "int";
        }

        if (type == TokenType.STRING_TYPE) {
            return "String";
        }

        throw new IllegalArgumentException(
                "Unsupported variable type: " + type
        );
    }

    private String escapeJavaString(String text) {

        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private void appendIndent(StringBuilder out) {

        for (int i = 0; i < indentLevel; i++) {
            out.append("    ");
        }
    }
}

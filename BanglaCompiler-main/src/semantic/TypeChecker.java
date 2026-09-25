package semantic;

import ast.*;
import lexer.TokenType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TypeChecker {

    private final Map<String, ValueType> symbols = new HashMap<>();
    private final List<SemanticError> errors = new ArrayList<>();

    public void check(List<Statement> statements) {
        for (Statement statement : statements) {
            checkStatement(statement);
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<SemanticError> getErrors() {
        return errors;
    }

    public Map<String, ValueType> getSymbols() {
        return symbols;
    }

    private void checkStatement(Statement statement) {

        if (statement instanceof VariableDeclaration declaration) {
            checkVariableDeclaration(declaration);
            return;
        }

        if (statement instanceof AssignmentStatement assignment) {
            checkAssignment(assignment);
            return;
        }

        if (statement instanceof IfStatement ifStatement) {
            checkCondition(
                    ifStatement.getCondition(),
                    "if"
            );

            checkBlock(ifStatement.getThenBranch());

            if (ifStatement.getElseBranch() != null) {
                checkBlock(ifStatement.getElseBranch());
            }

            return;
        }

        if (statement instanceof WhileStatement whileStatement) {
            checkCondition(
                    whileStatement.getCondition(),
                    "while"
            );

            checkBlock(whileStatement.getBody());
            return;
        }

        if (statement instanceof BlockStatement block) {
            checkBlock(block);
        }
    }

    private void checkVariableDeclaration(
            VariableDeclaration declaration) {

        String name = declaration.getName();

        if (symbols.containsKey(name)) {
            error("Variable '" + name + "' is already declared.");
            return;
        }

        ValueType declaredType =
                declaration.getType() == TokenType.INT
                        ? ValueType.INT
                        : ValueType.STRING;

        ValueType initializerType =
                checkExpression(
                        declaration.getInitializer()
                );

        if (initializerType != ValueType.ERROR
                && initializerType != declaredType) {

            error(
                    "Cannot assign "
                            + initializerType
                            + " to "
                            + declaredType
                            + " variable '"
                            + name
                            + "'."
            );
        }

        symbols.put(name, declaredType);
    }

    private void checkAssignment(
            AssignmentStatement assignment) {

        String name = assignment.getName();

        ValueType variableType =
                symbols.get(name);

        if (variableType == null) {
            error(
                    "Variable '"
                            + name
                            + "' is not declared."
            );

            checkExpression(assignment.getValue());
            return;
        }

        ValueType valueType =
                checkExpression(
                        assignment.getValue()
                );

        if (valueType != ValueType.ERROR
                && valueType != variableType) {

            error(
                    "Cannot assign "
                            + valueType
                            + " to "
                            + variableType
                            + " variable '"
                            + name
                            + "'."
            );
        }
    }

    private void checkCondition(
            Expression condition,
            String statementName) {

        ValueType conditionType =
                checkExpression(condition);

        if (conditionType != ValueType.ERROR
                && conditionType != ValueType.BOOLEAN) {

            error(
                    statementName
                            + " condition must be BOOLEAN, but found "
                            + conditionType
                            + "."
            );
        }
    }

    private void checkBlock(BlockStatement block) {
        for (Statement statement : block.getStatements()) {
            checkStatement(statement);
        }
    }

    private ValueType checkExpression(
            Expression expression) {

        if (expression instanceof LiteralExpression literal) {

            Object value = literal.getValue();

            if (value instanceof Integer) {
                return ValueType.INT;
            }

            if (value instanceof String) {
                return ValueType.STRING;
            }

            return ValueType.ERROR;
        }

        if (expression instanceof VariableExpression variable) {

            ValueType type =
                    symbols.get(variable.getName());

            if (type == null) {
                error(
                        "Variable '"
                                + variable.getName()
                                + "' is not declared."
                );

                return ValueType.ERROR;
            }

            return type;
        }

        if (expression instanceof BinaryExpression binary) {

            ValueType left =
                    checkExpression(binary.getLeft());

            ValueType right =
                    checkExpression(binary.getRight());

            TokenType operator =
                    binary.getOperator().getType();

            return switch (operator) {

                case PLUS,
                     MINUS,
                     STAR,
                     SLASH ->
                        checkArithmetic(
                                left,
                                right,
                                binary.getOperator().getLexeme()
                        );

                case LESS,
                     LESS_EQUAL,
                     GREATER,
                     GREATER_EQUAL ->
                        checkOrderedComparison(
                                left,
                                right,
                                binary.getOperator().getLexeme()
                        );

                case EQUAL_EQUAL,
                     NOT_EQUAL ->
                        checkEquality(
                                left,
                                right,
                                binary.getOperator().getLexeme()
                        );

                default -> ValueType.ERROR;
            };
        }

        return ValueType.ERROR;
    }

    private ValueType checkArithmetic(
            ValueType left,
            ValueType right,
            String operator) {

        if (left == ValueType.ERROR
                || right == ValueType.ERROR) {

            return ValueType.ERROR;
        }

        if (left != ValueType.INT
                || right != ValueType.INT) {

            error(
                    "Operator '"
                            + operator
                            + "' requires INT operands."
            );

            return ValueType.ERROR;
        }

        return ValueType.INT;
    }

    private ValueType checkOrderedComparison(
            ValueType left,
            ValueType right,
            String operator) {

        if (left == ValueType.ERROR
                || right == ValueType.ERROR) {

            return ValueType.ERROR;
        }

        if (left != ValueType.INT
                || right != ValueType.INT) {

            error(
                    "Operator '"
                            + operator
                            + "' requires INT operands."
            );

            return ValueType.ERROR;
        }

        return ValueType.BOOLEAN;
    }

    private ValueType checkEquality(
            ValueType left,
            ValueType right,
            String operator) {

        if (left == ValueType.ERROR
                || right == ValueType.ERROR) {

            return ValueType.ERROR;
        }

        if (left != right) {

            error(
                    "Operator '"
                            + operator
                            + "' requires operands of the same type."
            );

            return ValueType.ERROR;
        }

        return ValueType.BOOLEAN;
    }

    private void error(String message) {
        errors.add(new SemanticError(message));
    }
}

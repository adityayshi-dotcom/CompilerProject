import ast.*;
import lexer.Lexer;
import lexer.Token;
import parser.Parser;

import java.util.List;

public class ParserTest {

    public static void main(String[] args) {

        testCompleteProgram();
        testPrecedence();
        testSyntaxErrorRecovery();

        System.out.println();
        System.out.println("All parser tests passed.");
    }

    private static void testCompleteProgram() {

        String source = """
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
                """;

        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        if (lexer.hasErrors()) {
            throw new AssertionError(
                    "Lexer produced errors."
            );
        }

        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();

        if (parser.hasErrors()) {
            throw new AssertionError(
                    "Parser produced errors: "
                            + parser.getErrors()
            );
        }

        if (statements.size() != 5) {
            throw new AssertionError(
                    "Expected 5 top-level statements, got "
                            + statements.size()
            );
        }

        if (!(statements.get(0)
                instanceof VariableDeclaration)) {

            throw new AssertionError(
                    "First statement should be variable declaration."
            );
        }

        if (!(statements.get(3)
                instanceof IfStatement)) {

            throw new AssertionError(
                    "Fourth statement should be if statement."
            );
        }

        if (!(statements.get(4)
                instanceof WhileStatement)) {

            throw new AssertionError(
                    "Fifth statement should be while statement."
            );
        }

        System.out.println(
                "Complete program test passed."
        );
    }

    private static void testPrecedence() {

        Lexer lexer = new Lexer(
                "সংখ্যা ফল = 10 + 5 * 2;"
        );

        Parser parser =
                new Parser(lexer.tokenize());

        List<Statement> statements =
                parser.parse();

        VariableDeclaration declaration =
                (VariableDeclaration)
                        statements.get(0);

        BinaryExpression plus =
                (BinaryExpression)
                        declaration.getInitializer();

        if (!(plus.getRight()
                instanceof BinaryExpression)) {

            throw new AssertionError(
                    "Multiplication should be grouped before addition."
            );
        }

        BinaryExpression star =
                (BinaryExpression)
                        plus.getRight();

        if (!"*".equals(
                star.getOperator().getLexeme())) {

            throw new AssertionError(
                    "Expected multiplication on right side of addition."
            );
        }

        System.out.println(
                "Precedence test passed."
        );
    }

    private static void testSyntaxErrorRecovery() {

        Lexer lexer = new Lexer(
                "সংখ্যা বয়স = ; সংখ্যা x = 1;"
        );

        Parser parser =
                new Parser(lexer.tokenize());

        List<Statement> statements =
                parser.parse();

        if (!parser.hasErrors()) {
            throw new AssertionError(
                    "Expected a syntax error."
            );
        }

        if (statements.size() != 1) {
            throw new AssertionError(
                    "Parser should recover and parse the second declaration."
            );
        }

        System.out.println(
                "Syntax error recovery test passed."
        );

        System.out.println(
                parser.getErrors().get(0)
        );
    }
}

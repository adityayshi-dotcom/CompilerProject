import ast.Statement;
import lexer.Lexer;
import parser.Parser;
import semantic.TypeChecker;

import java.util.List;

public class TypeCheckerTest {

    public static void main(String[] args) {

        testValidProgram();
        testTypeMismatch();
        testUndeclaredVariable();
        testInvalidArithmetic();
        testInvalidCondition();

        System.out.println();
        System.out.println("All type checker tests passed.");
    }

    private static void testValidProgram() {

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

        TypeChecker checker = check(source);

        if (checker.hasErrors()) {
            throw new AssertionError(
                    "Valid program produced semantic errors: "
                            + checker.getErrors()
            );
        }

        System.out.println("Valid program test passed.");
    }

    private static void testTypeMismatch() {

        TypeChecker checker =
                check("সংখ্যা বয়স = \"রহিম\";");

        if (!checker.hasErrors()) {
            throw new AssertionError(
                    "Expected type mismatch error."
            );
        }

        System.out.println("Type mismatch test passed.");
        System.out.println(checker.getErrors().get(0));
    }

    private static void testUndeclaredVariable() {

        TypeChecker checker =
                check("x = 10;");

        if (!checker.hasErrors()) {
            throw new AssertionError(
                    "Expected undeclared variable error."
            );
        }

        System.out.println("Undeclared variable test passed.");
        System.out.println(checker.getErrors().get(0));
    }

    private static void testInvalidArithmetic() {

        String source = """
                লেখা নাম = "রহিম";
                সংখ্যা ফল = নাম + 1;
                """;

        TypeChecker checker = check(source);

        if (!checker.hasErrors()) {
            throw new AssertionError(
                    "Expected arithmetic type error."
            );
        }

        System.out.println("Invalid arithmetic test passed.");
        System.out.println(checker.getErrors().get(0));
    }

    private static void testInvalidCondition() {

        TypeChecker checker =
                check("""
                        সংখ্যা বয়স = 20;
                        যদি (বয়স) {
                            বয়স = বয়স + 1;
                        }
                        """);

        if (!checker.hasErrors()) {
            throw new AssertionError(
                    "Expected invalid condition error."
            );
        }

        System.out.println("Invalid condition test passed.");
        System.out.println(checker.getErrors().get(0));
    }

    private static TypeChecker check(String source) {

        Lexer lexer = new Lexer(source);

        Parser parser =
                new Parser(lexer.tokenize());

        List<Statement> statements =
                parser.parse();

        if (lexer.hasErrors()) {
            throw new AssertionError(
                    "Unexpected lexer errors: "
                            + lexer.getErrors()
            );
        }

        if (parser.hasErrors()) {
            throw new AssertionError(
                    "Unexpected parser errors: "
                            + parser.getErrors()
            );
        }

        TypeChecker checker =
                new TypeChecker();

        checker.check(statements);

        return checker;
    }
}

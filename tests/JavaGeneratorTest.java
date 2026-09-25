import ast.Statement;
import codegen.JavaGenerator;
import lexer.Lexer;
import parser.Parser;
import semantic.TypeChecker;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class JavaGeneratorTest {

    public static void main(String[] args) throws Exception {

        testGeneration();
        testIfElseAndWhile();

        System.out.println();
        System.out.println("All Java generator tests passed.");
    }

    private static void testGeneration() throws Exception {

        String source = """
                সংখ্যা বয়স = 20;
                সংখ্যা ফল = বয়স + 5 * 2;
                লেখা অবস্থা = "শিশু";
                """;

        String javaCode =
                generateValidJava(source);

        requireContains(
                javaCode,
                "int বয়স = 20;",
                "integer declaration"
        );

        requireContains(
                javaCode,
                "int ফল = (বয়স + (5 * 2));",
                "operator precedence"
        );

        requireContains(
                javaCode,
                "String অবস্থা = \"শিশু\";",
                "string declaration"
        );

        writeGeneratedFile(javaCode);

        System.out.println(
                "Basic generation test passed."
        );
    }

    private static void testIfElseAndWhile() {

        String source = """
                সংখ্যা বয়স = 20;
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

        String javaCode =
                generateValidJava(source);

        requireContains(
                javaCode,
                "if ((বয়স >= 18))",
                "if statement"
        );

        requireContains(
                javaCode,
                "else {",
                "else statement"
        );

        requireContains(
                javaCode,
                "while ((বয়স < 25))",
                "while statement"
        );

        requireContains(
                javaCode,
                "বয়স = (বয়স + 1);",
                "assignment"
        );

        System.out.println(
                "Control-flow generation test passed."
        );
    }

    private static String generateValidJava(
            String source) {

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

        if (checker.hasErrors()) {
            throw new AssertionError(
                    "Unexpected semantic errors: "
                            + checker.getErrors()
            );
        }

        JavaGenerator generator =
                new JavaGenerator();

        return generator.generateProgram(
                statements,
                "GeneratedProgram"
        );
    }

    private static void writeGeneratedFile(
            String javaCode) throws Exception {

        Path directory =
                Path.of("out", "generated");

        Files.createDirectories(directory);

        Files.writeString(
                directory.resolve(
                        "GeneratedProgram.java"
                ),
                javaCode,
                StandardCharsets.UTF_8
        );
    }

    private static void requireContains(
            String text,
            String expected,
            String name) {

        if (!text.contains(expected)) {
            throw new AssertionError(
                    "Missing "
                            + name
                            + ": "
                            + expected
                            + "\nGenerated code:\n"
                            + text
            );
        }
    }
}

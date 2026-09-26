import ast.Statement;
import codegen.JavaGenerator;
import lexer.Lexer;
import lexer.LexerError;
import lexer.Token;
import parser.Parser;
import parser.ParserError;
import semantic.SemanticError;
import semantic.TypeChecker;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class BanglaCompiler {

    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println(
                    "Usage: java -cp out BanglaCompiler <source.bng>"
            );
            return;
        }

        Path sourcePath = Path.of(args[0]);

        if (!Files.exists(sourcePath)) {
            System.out.println(
                    "Source file not found: " + sourcePath
            );
            return;
        }

        String source = Files.readString(
                sourcePath,
                StandardCharsets.UTF_8
        );

        // 1. Lexical analysis
        Lexer lexer = new Lexer(source);
        List<Token> tokens = lexer.tokenize();

        if (lexer.hasErrors()) {
            System.out.println("Lexical analysis failed:");

            for (LexerError error : lexer.getErrors()) {
                System.out.println(error);
            }

            return;
        }

        // 2. Parsing / AST
        Parser parser = new Parser(tokens);
        List<Statement> statements = parser.parse();

        if (parser.hasErrors()) {
            System.out.println("Parsing failed:");

            for (ParserError error : parser.getErrors()) {
                System.out.println(error);
            }

            return;
        }

        // 3. Semantic/type checking
        TypeChecker checker = new TypeChecker();
        checker.check(statements);

        if (checker.hasErrors()) {
            System.out.println("Semantic analysis failed:");

            for (SemanticError error : checker.getErrors()) {
                System.out.println(error);
            }

            return;
        }

        // 4. Java code generation
        JavaGenerator generator = new JavaGenerator();

        String javaCode = generator.generateProgram(
                statements,
                "GeneratedProgram"
        );

        Path generatedDir =
                Path.of("generated");

        Files.createDirectories(generatedDir);

        Path outputFile =
                generatedDir.resolve(
                        "GeneratedProgram.java"
                );

        Files.writeString(
                outputFile,
                javaCode,
                StandardCharsets.UTF_8
        );

        System.out.println(
                "Compilation successful."
        );

        System.out.println(
                "Generated Java file: "
                        + outputFile.toAbsolutePath()
        );
    }
}

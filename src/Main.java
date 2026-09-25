import lexer.Lexer;
import lexer.Token;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        String source = """
                সংখ্যা বয়স = 20;

                যদি (বয়স >= 18) {
                    লেখা অবস্থা = "প্রাপ্তবয়স্ক";
                }
                নাহলে {
                    লেখা অবস্থা = "শিশু";
                }

                যতক্ষণ (বয়স < 25) {
                    বয়স = বয়স + 1;
                }
                """;

        Lexer lexer = new Lexer(source);

        List<Token> tokens = lexer.tokenize();

        for (Token token : tokens) {
            System.out.println(token);
        }
    }
}
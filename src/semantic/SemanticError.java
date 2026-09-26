package semantic;

public class SemanticError {

    private final String message;

    public SemanticError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Semantic error: " + message;
    }
}

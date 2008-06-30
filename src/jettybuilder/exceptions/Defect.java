package jettybuilder.exceptions;

public class Defect extends RuntimeException{
    public Defect(String message, Throwable cause) {
        super(message, cause);
    }
}

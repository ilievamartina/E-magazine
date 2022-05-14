package mk.ukim.finki.emagazine.model.exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException() {
        super("User already exists exception!");
    }
}

package mk.ukim.finki.emagazine.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostNotFountException extends RuntimeException {
    public PostNotFountException() {
        super("Post not found!");
    }
}

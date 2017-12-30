package jhyun.jh.controllers.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Save URL not found!")
public class SavedUrlNotFoundException extends RuntimeException {
}

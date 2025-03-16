package jonatasSantos.royalLux.presentation.api.middlewares;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jonatasSantos.royalLux.core.application.exceptions.*;
import jonatasSantos.royalLux.presentation.api.presenters.ErrorResponsePresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class ExceptionHandlerMiddleware  {

    private ResponseEntity<ErrorResponsePresenter> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponsePresenter errorResponsePresenter = new ErrorResponsePresenter(status.getReasonPhrase(), ex.getMessage());
        return new ResponseEntity<>(errorResponsePresenter, status);
    }

    @ExceptionHandler({ResourceNotFoundException.class, UsernameNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponsePresenter> handleResourceNotFound(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponsePresenter> handleBadRequest(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UnauthorizedException.class, AuthenticationException.class, AccessDeniedException.class, DisabledException.class})
    public ResponseEntity<ErrorResponsePresenter> handleUnauthorized(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponsePresenter> handleForbidden(ForbiddenException exception) {
        return buildErrorResponse(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ConflictException.class, EntityExistsException.class})
    public ResponseEntity<ErrorResponsePresenter> handleConflict(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponsePresenter> handleGlobalException(Exception exception) {
        return buildErrorResponse(new InternalServerErrorException("An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

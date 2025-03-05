package jonatasSantos.royalLux.presentation.api.middlewares;

import jonatasSantos.royalLux.core.application.exceptions.*;
import jonatasSantos.royalLux.presentation.api.presenters.ErrorResponsePresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerMiddleware  {

    private ResponseEntity<ErrorResponsePresenter> buildErrorResponse(Exception ex, HttpStatus status) {
        ErrorResponsePresenter errorResponsePresenter = new ErrorResponsePresenter(status.getReasonPhrase(), ex.getMessage());
        return new ResponseEntity<>(errorResponsePresenter, status);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponsePresenter> handleResourceNotFound(ResourceNotFoundException exception) {
        return buildErrorResponse(exception, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorResponsePresenter> handleBadRequest(Exception exception) {
        return buildErrorResponse(exception, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponsePresenter> handleUnauthorized(UnauthorizedException exception) {
        return buildErrorResponse(exception, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponsePresenter> handleForbidden(ForbiddenException exception) {
        return buildErrorResponse(exception, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponsePresenter> handleConflict(ConflictException exception) {
        return buildErrorResponse(exception, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponsePresenter> handleGlobalException(Exception exception) {
        return buildErrorResponse(new InternalServerErrorException("An unexpected error occurred"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

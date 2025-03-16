package jonatasSantos.royalLux.presentation.api.presenters;

import java.time.LocalDateTime;

public class ErrorResponsePresenter {
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponsePresenter(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
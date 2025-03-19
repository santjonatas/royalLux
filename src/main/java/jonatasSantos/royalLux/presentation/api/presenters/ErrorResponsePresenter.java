package jonatasSantos.royalLux.presentation.api.presenters;

import java.time.LocalDateTime;

public class ErrorResponsePresenter {
    private String error;
    private String message;
    private Long timestamp;

    public ErrorResponsePresenter(String error, String message) {
        this.error = error;
        this.message = message;
        this.timestamp = System.currentTimeMillis();
    }

    public String getError() { return error; }
    public String getMessage() { return message; }
    public Long getTimestamp() { return timestamp; }
}
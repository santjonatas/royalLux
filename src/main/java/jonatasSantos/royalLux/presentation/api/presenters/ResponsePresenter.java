package jonatasSantos.royalLux.presentation.api.presenters;

import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

public class ResponsePresenter<T> extends RepresentationModel<ResponsePresenter<T>> {
    private T data;
    private LocalDateTime timestamp;

    public ResponsePresenter(T data) {
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LocalDateTime getTimestamp() { return this.timestamp; }

    public void setTimestamp() { this.timestamp = timestamp; }
}
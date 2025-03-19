package jonatasSantos.royalLux.presentation.api.presenters;

import org.springframework.hateoas.RepresentationModel;

public class ResponsePresenter<T> extends RepresentationModel<ResponsePresenter<T>> {
    private T data;
    private Long timestamp;

    public ResponsePresenter(T data) {
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() { return this.timestamp; }

    public void setTimestamp(Long timestamp) { this.timestamp = timestamp; }
}
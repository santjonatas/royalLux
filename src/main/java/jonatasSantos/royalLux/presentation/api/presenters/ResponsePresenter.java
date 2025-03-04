package jonatasSantos.royalLux.presentation.api.presenters;

import org.springframework.hateoas.RepresentationModel;

public class ResponsePresenter<T> extends RepresentationModel<ResponsePresenter<T>> {
    private T data;

    public ResponsePresenter(T data) {
        this.data = data;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

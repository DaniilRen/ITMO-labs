package common.transfer.request.wrapped;

import common.transfer.request.Request;

public abstract class HeaderRequest<T> implements Request {
    protected final Request innerRequest;
    protected T headers;

    public HeaderRequest(Request innerRequest) {
        this.innerRequest = innerRequest;
    }

    public HeaderRequest(Request innerRequest, T headers) {
        this.innerRequest = innerRequest;
        this.headers = headers;
    }

    public Request unwrap() {
        return innerRequest;
    }

    public void setHeaders(T headers) {
        this.headers = headers;
    }

    public T getHeaders() {
        return headers;
    }
}

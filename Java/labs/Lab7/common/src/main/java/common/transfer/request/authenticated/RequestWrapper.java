package common.transfer.request.authenticated;

import common.transfer.request.Request;

public abstract class RequestWrapper implements Request {
    protected final Request wrappedRequest;

    public RequestWrapper(Request wrappedRequest) {
        this.wrappedRequest = wrappedRequest;
    }

    public Request getWrappedRequest() {
        return wrappedRequest;
    }
}

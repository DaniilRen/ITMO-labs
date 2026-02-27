package util;

import java.util.List;


public class Response<T> extends Payload<T> {
    private Status status = Status.OK;

    public Response() {}

    public Response(List<T> body) {
        this.body = body;
    }

    public Response(Status status) {
        this.status = status;
    }

    public Response(List<T> body, Status status) {
        this.body = body;
        this.status = status;
    }

    public void setStatus(Status newStatus) {
        this.status = newStatus;
    }

    public Status getStatus() {
        return this.status;
    }

    public boolean IsEmpty() {
        return this.body.size() > 0;
    }
    
    public void put(T element) {
        body.add(element);
    };

    @Override
    public String toString() {
        return String.format("{\n\tstatus: %s,\n\tbody: %s\n}", status, body);
    }

};
package common.transfer.response;

import java.util.ArrayList;
import java.util.List;

import common.transfer.Status;


/**
 * Класс ответа.
 * @author Septyq
 * @param <T> объект, хранящийся в теле ответа
 */
public class Response<T> {
    private List<T> body = new ArrayList<>();
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

    public List<T> getBody() {
        return body;
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
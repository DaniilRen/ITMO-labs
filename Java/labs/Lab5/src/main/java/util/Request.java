package util;

import java.util.List;


public class Request<T> extends Payload<T> {
    private String name = "";

    public Request() {}

    public Request(String name) {
        this.name = name;
    }

    public Request(String name, List<T> body) {
        this.name = name;
        this.body = body;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
};

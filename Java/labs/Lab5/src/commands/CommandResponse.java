package commands;

import java.util.ArrayList;
import java.util.List;

import util.Response;
import util.Status;

public class CommandResponse<T> {
    private List<T> body = new ArrayList<>();

    public CommandResponse(List<T> body) {
        this.body = body;
    }

    public void put(T element) {
        body.add(element);
    };

    public Response wrapToResponse(Status status) {
        return new Response(body, status);
    }
};

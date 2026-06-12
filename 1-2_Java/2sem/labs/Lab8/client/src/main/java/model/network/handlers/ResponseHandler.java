package model.network.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import common.models.Entity;
import common.models.Route;
import common.transfer.Status;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.response.Response;
import model.network.AbstractClientNetwork;
import view.View;

public class ResponseHandler {
    private static final Set<String> MUTATING_COMMANDS =
            Set.of("add", "update", "remove_by_id", "clear", "remove_lower");

    private final View view;
    private final AbstractClientNetwork network;
    private final AuthHandler authHandler;

    public ResponseHandler(View view, AbstractClientNetwork network, AuthHandler authHandler) {
        this.view = view;
        this.network = network;
        this.authHandler = authHandler;
    }

    public Status handleResponse(Response<?> response) {
        return handleResponse(response, null);
    }

    public Status handleResponse(Response<?> response, String commandName) {
        if (response.isChunked()) {
            return handleChunkedResponse(response, commandName);
        }

        Status status = response.getStatus();
        if (status == Status.OK) {
            dispatchBody(response.getBody(), commandName);
        } else if (status == Status.ERROR) {
            List<?> body = response.getBody();
            if (body != null && !body.isEmpty()) {
                view.displayError(body.get(0).toString());
            } else {
                view.displayError("Unknown error occurred");
            }
        } else if (status == Status.LOGIN) {
            List<?> body = response.getBody();
            if ("register".equals(commandName) && authHandler.isAuthenticated()) {
                // Admin created another user — keep current session.
            } else {
                authHandler.setCredentials(
                        body.get(0).toString(), body.get(1).toString(), Boolean.parseBoolean(body.get(2).toString()));
            }
        }

        if (status == Status.OK && commandName != null && MUTATING_COMMANDS.contains(commandName)) {
            view.refreshCollectionView();
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    private Status handleChunkedResponse(Response<?> firstChunk, String commandName) {
        String streamId = firstChunk.getStreamId();
        int totalChunks = firstChunk.getTotalChunks();

        List<Object> allData = new ArrayList<>();

        if (firstChunk.getBody() != null) {
            allData.addAll((List<Object>) firstChunk.getBody());
        }

        for (int chunkNum = 2; chunkNum <= totalChunks; chunkNum++) {
            try {
                NextChunkRequest chunkRequest = new NextChunkRequest(streamId, chunkNum);
                network.write(chunkRequest);
                Response<?> nextChunk = (Response<?>) network.read();

                if (nextChunk.getStatus() == Status.ERROR) {
                    view.displayError("Failed to load chunk " + chunkNum);
                    return Status.ERROR;
                }

                if (nextChunk.getBody() != null) {
                    allData.addAll((List<Object>) nextChunk.getBody());
                }
            } catch (IOException | ClassNotFoundException e) {
                view.displayError("Failed to load chunk " + chunkNum + ": " + e.getMessage());
                return Status.ERROR;
            }
        }
        dispatchBody(allData, commandName);
        return Status.OK;
    }

    private void dispatchBody(List<?> body, String commandName) {
        if (body == null || body.isEmpty()) {
            return;
        }

        if (isEntityCollection(body)) {
            List<Entity> entities =
                    body.stream().filter(Entity.class::isInstance).map(Entity.class::cast).collect(Collectors.toList());
            view.onCollectionReceived(entities);
            return;
        }

        if ("show".equals(commandName)) {
            return;
        }

        String content = body.stream().map(Object::toString).collect(Collectors.joining("\n"));
        if (isDialogCommand(commandName)) {
            view.showTextDialog(commandName, content);
        } else {
            view.displayMessage(content);
        }
    }

    private boolean isEntityCollection(List<?> body) {
        return body.stream().anyMatch(Route.class::isInstance);
    }

    private boolean isDialogCommand(String commandName) {
        return commandName != null
                && Set.of("history", "help", "info", "filter_starts_with_name", "print_unique_distance", "print_field_descending_distance")
                        .contains(commandName);
    }
}

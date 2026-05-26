package model.network.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.transfer.Status;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.response.Response;
import model.network.AbstractClientNetwork;
import view.View;


public class ResponseHandler {
    private final View view;
    private final AbstractClientNetwork network;
    private final AuthHandler authHandler;

    public ResponseHandler(View view, AbstractClientNetwork network, AuthHandler authHandler) {
        this.view = view;
        this.network = network;
        this.authHandler = authHandler;
    }

    public Status handleResponse(Response<?> response) {
        if (response.isChunked()) return handleChunkedResponse(response);
        
        Status status = response.getStatus();
        if (status == Status.OK) {
            printCommandResponse(response.getBody());
        } else if (status == Status.ERROR) {
            List<?> body = response.getBody();
            if (body != null && !body.isEmpty()) {
                view.displayError(body.get(0).toString());
            } else {
                view.displayError("Unknown error occurred");
            }
        } else if (status == Status.LOGIN) {
            List<?> body = response.getBody();
            authHandler.setCredentials(body.get(0).toString(), body.get(1).toString(), Boolean.parseBoolean(body.get(2).toString()));
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    private Status handleChunkedResponse(Response<?> firstChunk) {
        String streamId = firstChunk.getStreamId();
        int totalChunks = firstChunk.getTotalChunks();
        
        List<Object> allData = new ArrayList<>();
        
        if (firstChunk.getBody() != null) {
            allData.addAll((List<Object>) firstChunk.getBody());
        }
        
        view.displayMessage(String.format("Loading data (%d chunks)...\n", totalChunks));
        int loadBarSectionIdx = totalChunks / 10;
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
                if (chunkNum % loadBarSectionIdx == 0) {
                    int percent = chunkNum * 100 / totalChunks;
                    view.displayMessage(percent + "% " +
                        "o".repeat(chunkNum / loadBarSectionIdx) + ".".repeat(10-(chunkNum / loadBarSectionIdx)));
                }                
            } catch (IOException | ClassNotFoundException e) {
                view.displayError("\nFailed to load chunk " + chunkNum + ": " + e.getMessage());
                return Status.ERROR;
            }
        }
        printCommandResponse(allData);
        return Status.OK;
    }

    private void printCommandResponse(List<?> body) {
        if (body != null && !body.isEmpty()) {
            body.forEach((element) -> {
                view.displayMessage(element.toString());
            });
        }
    }
}

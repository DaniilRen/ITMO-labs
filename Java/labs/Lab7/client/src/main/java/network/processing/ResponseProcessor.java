package network.processing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import common.transfer.Status;
import common.transfer.request.standart.NextChunkRequest;
import common.transfer.response.Response;
import console.IOConsole;
import network.AbstractClientNetwork;


public class ResponseProcessor {
    private final IOConsole console;
    private final AbstractClientNetwork network;
    private final AuthHandler authHandler;

    public ResponseProcessor(IOConsole console, AbstractClientNetwork network, AuthHandler authHandler) {
        this.console = console;
        this.network = network;
        this.authHandler = authHandler;
    }

    public Status processCommandResponse(Response<?> response) {
        if (response.isChunked()) return processChunkedResponse(response);
        
        Status status = response.getStatus();
        if (status == Status.OK) {
            printCommandResponse(response.getBody());
        } else if (status == Status.ERROR) {
            List<?> body = response.getBody();
            if (body != null && !body.isEmpty()) {
                console.printError(body.get(0).toString());
            } else {
                console.printError("Unknown error occurred");
            }
        } else if (status == Status.LOGIN) {
            authHandler.setAuthenticated(true);
            List<?> body = response.getBody();
            authHandler.setCredentials(body.get(0).toString(), body.get(1).toString());
        }
        return status;
    }

    @SuppressWarnings("unchecked")
    private Status processChunkedResponse(Response<?> firstChunk) {
        String streamId = firstChunk.getStreamId();
        int totalChunks = firstChunk.getTotalChunks();
        
        List<Object> allData = new ArrayList<>();
        
        if (firstChunk.getBody() != null) {
            allData.addAll((List<Object>) firstChunk.getBody());
        }
        
        console.println(String.format("Loading data (%d chunks)...\n", totalChunks));
        int loadBarSectionIdx = totalChunks / 10;
        for (int chunkNum = 2; chunkNum <= totalChunks; chunkNum++) {
            try {
                NextChunkRequest chunkRequest = new NextChunkRequest(streamId, chunkNum);
                network.write(chunkRequest);
                Response<?> nextChunk = (Response<?>) network.read();
                
                if (nextChunk.getStatus() == Status.ERROR) {
                    console.printError("Failed to load chunk " + chunkNum);
                    return Status.ERROR;
                }
                
                if (nextChunk.getBody() != null) {
                    allData.addAll((List<Object>) nextChunk.getBody());
                }
                if (chunkNum % loadBarSectionIdx == 0) {
                    int percent = chunkNum * 100 / totalChunks;
                    console.println(percent + "% " +
                        "o".repeat(chunkNum / loadBarSectionIdx) + ".".repeat(10-(chunkNum / loadBarSectionIdx)));
                }                
            } catch (IOException | ClassNotFoundException e) {
                console.printError("\nFailed to load chunk " + chunkNum + ": " + e.getMessage());
                return Status.ERROR;
            }
        }
        printCommandResponse(allData);
        return Status.OK;
    }

    private void printCommandResponse(List<?> body) {
        if (body != null && !body.isEmpty()) {
            body.forEach((element) -> {
                console.println(element);
            });
        }
    }
}

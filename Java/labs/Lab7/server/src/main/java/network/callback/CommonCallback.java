package network.callback;

import java.io.IOException;

import common.transfer.request.Request;
import common.transfer.response.Response;
import network.handlers.ClientHandler;
import network.handlers.RequestHandler;
import util.logging.AbstractLogger;

public class CommonCallback implements AbstractCallback {
    private final AbstractLogger logger;
    private final RequestHandler requestHandler;

    public CommonCallback(RequestHandler requestHandler, AbstractLogger logger) {
        this.requestHandler = requestHandler;
        this.logger = logger;
    }

    @Override
    public void onMessageReceived(ClientHandler client, Object message) {
        try {
            Request request = (Request) message;
            logger.info("new request from " + client.getClientSocket().getInetAddress() + ": " + request);
            
            Response<?> response = requestHandler.handleRequest(request);
            logger.info("sending response: " + response);
            
            client.write(response);
            
        } catch (ClassCastException e) {
            logger.error("Invalid request type received", e);
        } catch (IOException e) {
            logger.error("Failed to send response to client", e);
        } catch (Exception e) {
            logger.error("Error handling request", e);
        }
    }
    
    @Override
    public void onClientConnected(ClientHandler client) {
        logger.info("Client connected: " + client.getClientSocket().getInetAddress());
    }
    
    @Override
    public void onClientDisconnected(ClientHandler client) {
        logger.info("Client disconnected: " + client.getClientSocket().getInetAddress());
    }
}

package model;

import java.io.IOException;
import java.util.List;

import common.exceptions.InvalidAttributesException;
import common.exceptions.InvalidScriptException;
import common.transfer.Status;
import common.transfer.response.Response;
import model.script.ScriptProcessor;
import model.network.AbstractClientNetwork;
import model.network.ClientNetwork;
import model.network.handlers.AuthHandler;
import model.network.handlers.RequestHandler;
import model.network.handlers.ResponseHandler;
import view.View;

public class Model {
    private ScriptProcessor scriptProcessor;
    private AbstractClientNetwork network;
    private ResponseHandler responseHandler;
    private RequestHandler requestHandler;
    private AuthHandler authHandler;
    private View currentView;

    public Model(View currentView) {
        this.currentView = currentView;
    }

    public void startConnection(String address, int port) {
        try {
            this.scriptProcessor = new ScriptProcessor(currentView);

            this.network = new ClientNetwork(address, port, currentView);
            
            this.authHandler = new AuthHandler();
            this.responseHandler = new ResponseHandler(currentView, network, authHandler);
            this.requestHandler = new RequestHandler(currentView, network, authHandler, scriptProcessor);

            network.connect();
            requestHandler.setCommandAttributes();
        } catch (IOException e) {
            currentView.displayError("Failed to connect to server: " + e.getMessage());
            return;
        } catch (InvalidAttributesException e) {
            currentView.displayError("(Init Request): " + e.getMessage());
            return;
        }
        currentView.displayMessage(String.format("<----- client started on port: %d ----->", port));
    }

    public void stopConnection() {
        network.close();
    }

    public Status executeCommand(String commandName, List<?> args, boolean fileMode) {
        if (commandName == null || commandName.isEmpty()) return Status.ERROR;

        if (commandName.equals("exit")) {
            return Status.EXIT;
        }
        
        if (commandName.equals("execute_script")) {
            if (args.isEmpty()) {
                currentView.displayError("No script file specified");
                return Status.ERROR;
            }
            try {
                scriptProcessor.executeScript((String) args.get(0));
                return Status.OK;
            } catch (InvalidScriptException | ClassCastException e) {
                currentView.displayError(e.getMessage());
                return Status.ERROR;
            }
        }
        Response<?> response = requestHandler.makeRequest(commandName, args, fileMode);
        return responseHandler.handleResponse(response);
    }
}

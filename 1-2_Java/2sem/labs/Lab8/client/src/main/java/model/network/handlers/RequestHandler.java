package model.network.handlers;

import java.io.IOException;
import java.util.List;

import common.exceptions.IncorrectRequestException;
import common.exceptions.InvalidScriptException;
import common.transfer.Status;
import common.transfer.request.Request;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;
import model.network.AbstractClientNetwork;
import model.RequestBuilder;
import model.local.script.ScriptProcessor;
import view.View;

public class RequestHandler {
    private final View view;
    private final AbstractClientNetwork network;
    private final AuthHandler authHandler;
    private ScriptProcessor scriptProcessor;

    public RequestHandler(View view, AbstractClientNetwork network, AuthHandler authHandler, ScriptProcessor scriptProcessor) {
        this.view = view;
        this.network = network;
        this.authHandler = authHandler;
        this.scriptProcessor = scriptProcessor;
    }

    public Response<?> makeRequest(String name, Class<? extends Request> requestType, List<?> args, boolean fileMode) {
        try {
            if (name.equals("init") && args.size() == 0) {
                Request request = new StandartRequest(name);
                return sendRequest(authHandler.wrapCredentials(request));
            }

            if (requestType == null) {
                return new Response<>(List.of("Invalid request type"), Status.ERROR);
            }
  
            RequestBuilder requestBuilder = new RequestBuilder(view, authHandler.getCredentials().getName(), fileMode, scriptProcessor);
            Request request = requestBuilder.buildRequest(requestType, name, args);
            request = authHandler.wrapCredentials(request);

            return sendRequest(request);
        } catch (IncorrectRequestException e) {
            return new Response<>(List.of(e.getMessage()), Status.ERROR);
        } catch (InvalidScriptException e) {
            scriptProcessor.stopScript();
            return new Response<>(List.of(e.getMessage()));
        }
    }

    private Response<?> sendRequest(Request request) {
        try {
            network.write(request);
            return (Response<?>) network.read();
        } catch (IOException e) {            
            try {
                view.displayError("Connection lost: attempting to reconnect...");
                network.close();
                network.connect();
                
                network.write(request);
                Response<?> response = (Response<?>) network.read();
                return response;
                
            } catch (IOException | ClassNotFoundException ex) {
                view.displayError("Failed to reconnect: " + ex.getMessage());
                return new Response<>(List.of("Server unavailable"), Status.ERROR);
            }
            
        } catch (ClassNotFoundException e) {
            return new Response<>(List.of("Protocol error"), Status.ERROR);
        }
    }
}

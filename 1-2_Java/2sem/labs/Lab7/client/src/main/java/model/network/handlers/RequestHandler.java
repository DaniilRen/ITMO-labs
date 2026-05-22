package model.network.handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import common.exceptions.IncorrectRequestException;
import common.exceptions.InvalidAttributesException;
import common.exceptions.InvalidScriptException;
import common.transfer.Status;
import common.transfer.request.Request;
import common.transfer.request.standart.AuthRequest;
import common.transfer.request.standart.InitRequest;
import common.transfer.response.Response;
import model.network.AbstractClientNetwork;
import model.RequestBuilder;
import model.script.ScriptProcessor;
import view.View;

public class RequestHandler {
    private Map<String, Class<? extends Request>> commandsAttributes = new HashMap<>(); 
    private final View view;
    private final AbstractClientNetwork network;
    private final AuthHandler authHandler;
    private ScriptProcessor scriptProcessor;
    private final List<Class<? extends Request>> publicRequests = new ArrayList<>(Arrays.asList(AuthRequest.class, InitRequest.class));

    public RequestHandler(View view, AbstractClientNetwork network, AuthHandler authHandler, ScriptProcessor scriptProcessor) {
        this.view = view;
        this.network = network;
        this.authHandler = authHandler;
        this.scriptProcessor = scriptProcessor;
    }

    public Response<?> makeRequest(String name, List<?> args, boolean fileMode) {
        try {
            if (name.equals("logout") && args.size() == 0) {
                authHandler.logOut();
                return new Response<>(List.of("Logged out"));
            }

            Class<? extends Request> requestType = getRequestType(name);
            if (requestType == null) {
                return new Response<>(List.of("Invalid command!"), Status.ERROR);
            }
  
            if (!(authHandler.isAuthenticated()) && (!(publicRequests.contains(requestType)))) {
                return new Response<>(List.of("You must 'login' or 'register' before executing commands!"), Status.ERROR);
            }
            RequestBuilder requestBuilder = new RequestBuilder(view, authHandler.getCredentials().getName(), fileMode, scriptProcessor);
            Request request = requestBuilder.buildRequest(requestType, name, args);
            request = authHandler.wrapCredentials(request);

            return sendRequest(request);
        } catch (IncorrectRequestException e) {
            return new Response<>(List.of(e.getMessage()), Status.ERROR);
        } catch (InvalidScriptException e) {
            return new Response<>(List.of(e.getMessage()), Status.EXIT);
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

    @SuppressWarnings("unchecked")
    public void setCommandAttributes() throws InvalidAttributesException {
        Response<?> response = makeRequest("init", new ArrayList<>(), false);

        List<?> body = response.getBody();
    
        if (body == null || body.isEmpty()) {
            throw new InvalidAttributesException("Empty response body");
        }

        Object item = body.get(0);
        if (!(item instanceof Map<?, ?>)) {
            throw new InvalidAttributesException("Expected attributes Map<?, ?>, but received: " + item);
        }

        commandsAttributes = (Map<String, Class<? extends Request>>) item;
    }

    private Class<? extends Request> getRequestType(String name) {
        Class<? extends Request> requestType = commandsAttributes.get(name);
        if ((requestType == null) && (name.equals("init"))) return InitRequest.class;
        return requestType;
    }
}

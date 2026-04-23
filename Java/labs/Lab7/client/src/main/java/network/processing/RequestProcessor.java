package network.processing;

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
import common.transfer.request.empty.InitRequest;
import common.transfer.request.standart.AuthRequest;
import common.transfer.response.Response;
import console.IOConsole;
import network.AbstractClientNetwork;
import util.RequestBuilder;

public class RequestProcessor {
    private Map<String, Class<? extends Request>> commandsAttributes = new HashMap<>(); 
    private final IOConsole console;
    private final AbstractClientNetwork network;
    private final AuthHandler authHandler;
    private final List<Class<? extends Request>> alowedRequests = new ArrayList<>(Arrays.asList(AuthRequest.class, InitRequest.class));

    public RequestProcessor(IOConsole console, AbstractClientNetwork network, AuthHandler authHandler) {
        this.console = console;
        this.network = network;
        this.authHandler = authHandler;
    }

    public Response<?> makeRequest(String name, List<?> args) {
        Request request;
        if (name.equals("init")) {
            request = new InitRequest();
        } else {
            Class<? extends Request> requestType = commandsAttributes.get(name);
            if (!(authHandler.isAuthenticated()) && (!(alowedRequests.contains(requestType)))) {
                return new Response<>(List.of("You must login before executing commands!"), Status.ERROR);
            }

            try {
                RequestBuilder requestBuilder = new RequestBuilder(console);
                request = requestBuilder.buildRequest(requestType, name, args);
                if (!(alowedRequests.contains(requestType))) {
                    request = authHandler.addCredentials(request);
                }
            } catch (IncorrectRequestException e) {
                return new Response<>(List.of(e.getMessage()), Status.ERROR);
            } catch (InvalidScriptException e) {
                return new Response<>(List.of(e.getMessage()), Status.EXIT);
            }
        }

        return sendRequestToServer(request);
    }

    private Response<?> sendRequestToServer(Request request) {
        try {
            network.write(request);
            return (Response<?>) network.read();
        } catch (IOException e) {            
            try {
                console.printConnectionError("Connection lost: attempting to reconnect...");
                network.close();
                network.connect();
                
                network.write(request);
                return (Response<?>) network.read();
                
            } catch (IOException | ClassNotFoundException ex) {
                console.printConnectionError("Failed to reconnect: " + ex.getMessage());
                return new Response<>(List.of("Server unavailable"), Status.ERROR);
            }
            
        } catch (ClassNotFoundException e) {
            return new Response<>(List.of("Protocol error"), Status.ERROR);
        }
    }

    @SuppressWarnings("unchecked")
    public void setCommandAttributes() throws InvalidAttributesException {
        Response<?> response = makeRequest("init", new ArrayList<>());

        List<?> body = response.getBody();
    
        if (body == null || body.isEmpty()) {
            throw new InvalidAttributesException("Empty response body");
        }

        Object item = body.get(0);
        if (!(item instanceof Map<?, ?>)) {
            throw new InvalidAttributesException("Expected Map, got: " + 
                (item != null ? item.getClass().getSimpleName() : "null"));
        }

        commandsAttributes = (Map<String, Class<? extends Request>>) item;
    }
}

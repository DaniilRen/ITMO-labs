package util;

import java.util.List;
import java.util.Map;

import common.models.Entity;
import console.IOConsole;
import common.exceptions.IncorrectRequestException;
import common.exceptions.InvalidScriptException;
import forms.RouteForm;
import common.transfer.request.Request;
import common.transfer.request.standart.CombinedRequest;
import common.transfer.request.standart.EntityRequest;
import common.transfer.request.standart.IdRequest;
import common.transfer.request.standart.StandartRequest;
import common.transfer.request.standart.StringRequest;


/**
 * Создание запроса необходимого формата и валидация аргументов.
 * @author Septyq
 */
public class RequestBuilder {
    private final IOConsole console;

    public RequestBuilder(IOConsole console) {
        this.console = console;
    }

    public Request buildRequest(Map<String, Class<? extends Request>> commandsAttributes, String name, List<?> args) 
                                        throws IncorrectRequestException, InvalidScriptException {

        Class<? extends Request> requestType = commandsAttributes.get(name);
        if (requestType == null) {
            throw new IncorrectRequestException("Unknown command: " + name);
        }

        if (requestType == StandartRequest.class && args.size() == 0) {
            return new StandartRequest(name);
        } else if (requestType == StringRequest.class && StringRequest.validate(args)) {
            return new StringRequest(name, (String) args.get(0));
        } else if (requestType == IdRequest.class && IdRequest.validate(args)) {
            return new IdRequest(name, Integer.valueOf((String) args.get(0)));
        } else if (requestType == EntityRequest.class) {
            Entity result = buildEntity();

            if (result == null || !EntityRequest.validate(List.of(result))) {
                if (console.fileMode()) {
                    throw new InvalidScriptException("Stopped script running because of invalid syntax");
                }
                throw new IncorrectRequestException("Invalid request");
            }
            return new EntityRequest(name, result);
        } else if (requestType == CombinedRequest.class && args.size() == 1) {
            try {
                Integer id = Integer.valueOf((String) args.get(0));
                Entity result = buildEntity();
                if (result == null || !CombinedRequest.validate(List.of(result, id))) {
                    throw new IncorrectRequestException("Invalid request");
                }
                return new CombinedRequest(name, result, id);
            } catch (NumberFormatException e) {
                throw new IncorrectRequestException("Invalid request");
            }
        }
        
        throw new IncorrectRequestException("Invalid request");
    }


    private Entity buildEntity() {
        RouteForm form = new RouteForm(console);
        return form.build();
    }
}

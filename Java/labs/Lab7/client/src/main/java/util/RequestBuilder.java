package util;

import java.util.List;

import common.models.Entity;
import console.IOConsole;
import common.exceptions.IncorrectRequestException;
import common.exceptions.InvalidScriptException;
import forms.RouteForm;
import forms.UserDataForm;
import common.transfer.request.Request;
import common.transfer.request.standart.AuthRequest;
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

    public Request buildRequest(Class<? extends Request> requestType, String name, List<?> args) 
                                        throws IncorrectRequestException, InvalidScriptException {
        if (requestType == null) {
            throw new IncorrectRequestException("Unknown command: " + name);
        }

        if (requestType == StandartRequest.class && args.size() == 0) {
            return new StandartRequest(name);
        } else if (requestType == StringRequest.class && StringRequest.validate(args)) {
            return new StringRequest(name, args.get(0).toString());
        } else if (requestType == IdRequest.class && IdRequest.validate(args)) {
            return new IdRequest(name, Integer.valueOf(args.get(0).toString()));
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
                Integer id = Integer.valueOf(args.get(0).toString());
                Entity result = buildEntity();
                if (result == null || !CombinedRequest.validate(List.of(result, id))) {
                    throw new IncorrectRequestException("Invalid request");
                }
                return new CombinedRequest(name, result, id);
            } catch (NumberFormatException e) {
                throw new IncorrectRequestException("Invalid request");
            }
        } else if (requestType == AuthRequest.class && args.size() == 0) {
            UserData result = buildUserData(); 
            if (result == null || !AuthRequest.validate(List.of(result.user(), result.password()))) {
                throw new IncorrectRequestException("Invalid request");
            }
            return new AuthRequest(name, result.user(), result.password());
        }
        
        throw new IncorrectRequestException("Invalid request");
    }


    private Entity buildEntity() {
        return new RouteForm(console).build();
    }

    private UserData buildUserData() {
        return new UserDataForm(console).build();
    }
}

package model;

import java.util.List;

import common.models.Entity;
import common.models.User;
import common.exceptions.IncorrectRequestException;
import common.exceptions.InvalidScriptException;
import view.View;
import common.transfer.request.Request;
import common.transfer.request.standart.AuthRequest;
import common.transfer.request.standart.CombinedRequest;
import common.transfer.request.standart.EntityRequest;
import common.transfer.request.standart.IdRequest;
import common.transfer.request.standart.StandartRequest;
import common.transfer.request.standart.StringRequest;
import model.local.script.ScriptProcessor;


/**
 * Создание запроса необходимого формата и валидация аргументов.
 * @author Septyq
 */
public class RequestBuilder {
    private final View view;
    private final String author;
    private final boolean fileMode;
    private ScriptProcessor scriptProcessor;

    public RequestBuilder(View view, String author, boolean fileMode, ScriptProcessor scriptProcessor) {
        this.view = view;
        this.author = author;
        this.fileMode = fileMode;
        this.scriptProcessor = scriptProcessor;
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
            Entity result = buildEntity(author);

            if (result == null || !EntityRequest.validate(List.of(result))) {
                throw new IncorrectRequestException("Invalid request");
            }
            return new EntityRequest(name, result);
        } else if (requestType == CombinedRequest.class && args.size() == 1) {
            try {
                Integer id = Integer.valueOf(args.get(0).toString());
                Entity result = buildEntity(author);
                if (result == null || !CombinedRequest.validate(List.of(result, id))) {
                    throw new IncorrectRequestException("Invalid request");
                }
                return new CombinedRequest(name, result, id);
            } catch (NumberFormatException e) {
                throw new IncorrectRequestException("Invalid request");
            }
        } else if (requestType == AuthRequest.class && args.size() == 0) {
            User result = buildUser(); 
            if (result == null || !AuthRequest.validate(List.of(result.getName(), result.getPassword()))) {
                throw new IncorrectRequestException("Invalid request");
            }
            return new AuthRequest(name, result.getName(), result.getPassword());
        }
        
        throw new IncorrectRequestException("Invalid request");
    }

    private Entity buildEntity(String author) {
        if (fileMode) {
            return scriptProcessor.onEntityAdd(author);
        }
        return view.onEntityAdd(author);
    }

    private User buildUser() {
        if (fileMode) {
            return scriptProcessor.onUserAdd();
        }
        return view.onUserAdd();
    }
}

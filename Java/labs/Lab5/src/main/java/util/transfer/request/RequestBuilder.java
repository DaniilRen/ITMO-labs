package util.transfer.request;

import java.util.List;
import java.util.Map;

import models.Entity;
import util.console.IOConsole;
import util.exceptions.IncorrectRequestException;
import util.forms.RouteForm;
import util.transfer.request.standart.CombinedRequest;
import util.transfer.request.standart.EntityRequest;
import util.transfer.request.standart.IdRequest;
import util.transfer.request.standart.StandartRequest;
import util.transfer.request.standart.StringRequest;


/**
 * Создание запроса необходимого формата и валидация аргументов.
 * @author Septyq
 */
public class RequestBuilder {
    public static Request buildRequest(Map<String, Class<? extends Request>> commandsAttributes, 
                                        String name, List<?> args, IOConsole console) 
                                        throws IncorrectRequestException {

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
            Entity result = buildEntity(console);

            if (result == null || !EntityRequest.validate(List.of(result))) {
                throw new IncorrectRequestException("Invalid request");
            }
            return new EntityRequest(name, result);
        } else if (requestType == CombinedRequest.class && args.size() == 1) {
            try {
                Integer id = Integer.valueOf((String) args.get(0));
                Entity result = buildEntity(console);
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


    private static Entity buildEntity(IOConsole console) {
        RouteForm form = new RouteForm(console);
        return form.build();
    }
}

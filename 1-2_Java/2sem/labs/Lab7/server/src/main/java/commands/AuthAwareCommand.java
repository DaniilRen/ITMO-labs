package commands;

import common.models.User;
import common.transfer.request.Request;
import common.transfer.response.Response;

/**
 * Абстрактная класс для реализации выполнения команды с запросом данных пользователя
 * @param <T> тип запроса
 * @author Septyq
 */
public abstract class AuthAwareCommand<T extends Request> extends Command<T> {
    public AuthAwareCommand(CommandAttribute commandAttribute) {
        super(commandAttribute);
    }

    private static final long serialVersionUID = 98723097272L;
    
    public Response<?> execute(T request) {
        return this.execute(request, null);
    }

    public abstract Response<?> execute(T request, User userData);
}

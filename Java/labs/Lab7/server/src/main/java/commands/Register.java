package commands;

import java.util.List;

import auth.AuthManager;
import common.exceptions.AuthException;
import common.transfer.Status;
import common.transfer.request.standart.AuthRequest;
import common.transfer.response.Response;

/**
 * Команда 'register'. Регистрирует нового пользователя.
 * @author Septyq
 */
public class Register extends Command<AuthRequest> {
    private static final long serialVersionUID = 8932432L;

    private final AuthManager authManager;

    public Register(AuthManager authManager) {
        super(new CommandAttribute(
            "register", 
            "зарегистрировать нового пользователя", 
            AuthRequest.class
            ));
        this.authManager = authManager;
    }

    public Response<?> execute(AuthRequest request) {
        try {
            authManager.register(request.getUserName(), request.getPassword());
            return new Response<>(List.of(request.getUserName(), request.getPassword()), Status.LOGIN);
        } catch (AuthException e) {
            return new Response<>(List.of(e.getMessage()), Status.ERROR);
        }
    }
}
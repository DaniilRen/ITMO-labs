package commands;

import java.util.List;

import auth.AuthManager;
import common.exceptions.AuthException;
import common.transfer.Status;
import common.transfer.request.standart.AuthRequest;
import common.transfer.response.Response;

/**
 * Команда 'login'. Вход в аккаунт пользователя.
 * @author Septyq
 */
public class Authenticate extends Command<AuthRequest> {
    private static final long serialVersionUID = 8932432L;

    private final AuthManager authManager;

    public Authenticate(AuthManager authManager) {
        super(new CommandAttribute(
            "login", 
            "аутентификация пользователя", 
            AuthRequest.class
            ));
        this.authManager = authManager;
    }

    public Response<?> execute(AuthRequest request) {
        try {
            authManager.authenticate(request.getUserName(), request.getPassword());
            return new Response<>(List.of(request.getUserName(), request.getPassword()), Status.LOGIN);
        } catch (AuthException e) {
            return new Response<>(List.of(e.getMessage()), Status.ERROR);
        }
    }
}

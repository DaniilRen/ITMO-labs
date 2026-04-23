package commands;

import java.util.List;

import common.exceptions.AuthException;
import common.transfer.Status;
import common.transfer.request.standart.AuthRequest;
import common.transfer.response.Response;
import managers.auth.AbstractAuthManager;


public class Register extends Command<AuthRequest> {
    private static final long serialVersionUID = 8932432L;

    private final AbstractAuthManager authManager;

    public Register(AbstractAuthManager authManager) {
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
            return new Response<>(List.of("registered new user: " + request.getUserName()));
        } catch (AuthException e) {
            return new Response<>(List.of(e.getMessage()), Status.ERROR);
        }
    }
}
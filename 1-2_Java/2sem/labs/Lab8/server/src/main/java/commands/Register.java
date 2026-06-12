package commands;

import java.sql.SQLException;
import java.util.List;

import auth.AuthManager;
import common.command.CommandAttribute;
import common.command.PublicityMarker;
import common.exceptions.AuthException;
import common.models.User;
import common.transfer.Status;
import common.transfer.request.standart.RegisterRequest;
import common.transfer.response.Response;
import database.service.DatabaseService;

/**
 * Команда 'register'. Регистрирует нового пользователя.
 * @author Septyq
 */
public class Register extends Command<RegisterRequest> {
    private static final long serialVersionUID = 8932432L;

    private final AuthManager authManager;
    private final DatabaseService databaseService;

    public Register(AuthManager authManager, DatabaseService databaseService) {
        super(new CommandAttribute(
            "register", 
            "зарегистрировать нового пользователя", 
            RegisterRequest.class,
            PublicityMarker.PUBLIC
            ));
        this.authManager = authManager;
        this.databaseService = databaseService;
    }

    public Response<?> execute(RegisterRequest request) {
        try {
            authManager.register(request.getCredetials());
            User user = databaseService.getUserByName(request.getUserName());
            return new Response<>(List.of(user.getName(), request.getPassword(), user.getIsAdmin()), Status.LOGIN);
        } catch (AuthException | SQLException e) {
            return new Response<>(List.of(e.getMessage()), Status.ERROR);
        }
    }
}
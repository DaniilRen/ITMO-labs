package commands;

import java.sql.SQLException;
import java.util.List;

import auth.AuthManager;
import common.command.CommandAttribute;
import common.command.PublicityMarker;
import common.exceptions.AuthException;
import common.models.User;
import common.transfer.Status;
import common.transfer.request.standart.AuthRequest;
import common.transfer.response.Response;
import database.service.DatabaseService;

/**
 * Команда 'login'. Вход в аккаунт пользователя.
 * @author Septyq
 */
public class Authenticate extends Command<AuthRequest> {
    private static final long serialVersionUID = 8932432L;

    private final DatabaseService databaseService;
    private final AuthManager authManager;

    public Authenticate(AuthManager authManager, DatabaseService databaseService) {
        super(new CommandAttribute(
            "login", 
            "аутентификация пользователя", 
            AuthRequest.class,
            PublicityMarker.PUBLIC
            ));
        this.authManager = authManager;
        this.databaseService = databaseService;
    }

    public Response<?> execute(AuthRequest request) {
        try {
            authManager.authenticate(request.getCredetials());
            User user = databaseService.getUserByName(request.getUserName());
            return new Response<>(List.of(user.getName(), request.getPassword(), user.getIsAdmin()), Status.LOGIN);
        } catch (AuthException | SQLException e) {
            return new Response<>(List.of(e.getMessage()), Status.ERROR);
        }
    }
}

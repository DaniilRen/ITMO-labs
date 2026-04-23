package commands;

import java.util.ArrayList;
import java.util.List;

import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;
import managers.commands.AbstractCommandManager;

/**
 * Команда 'history'. Выводит последние 13 команд (без их аргументов).
 * @author Septyq
 */
public class History extends Command<StandartRequest> {
    private static final long serialVersionUID = 436542L;

    private final AbstractCommandManager commandManager;

    public History(AbstractCommandManager commandManager) {
        super(new CommandAttribute(
            "history", 
            "вывести последние 13 команд (без их аргументов)",
            StandartRequest.class
            ));
        this.commandManager = commandManager;
    }

    public Response<?> execute(StandartRequest request) {
        List<String> history = (List<String>) commandManager.getCommandHistory(13);
        List<String> body = new ArrayList<>(history);
        return new Response<>(body);
    }
}

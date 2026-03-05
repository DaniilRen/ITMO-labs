package commands;

import managers.CommandManager;
import util.transfer.request.standart.StandartRequest;
import util.transfer.response.Response;

/**
 * Команда 'history'. Выводит последние 13 команд (без их аргументов).
 * @author Septyq
 */
public class History extends Command<StandartRequest> {
    private final CommandManager commandManager;

    public History(CommandManager commandManager) {
        super(new CommandAttribute(
            "help", 
            "вывести последние 13 команд (без их аргументов)",
            StandartRequest.class
            ));
        this.commandManager = commandManager;
    }

    public Response<?> execute(StandartRequest request) {
        return new Response<>(commandManager.getCommandHistory(13));
    }
}

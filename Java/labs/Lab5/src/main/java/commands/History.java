package commands;

import java.util.List;

import managers.CommandManager;
import util.Response;
import util.Status;

public class History extends Command {
    private final CommandManager commandManager;

    public History(CommandManager commandManager) {
        super("help", "вывести последние 13 команд (без их аргументов)");
        this.commandManager = commandManager;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() != 0) {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        } else {
            return new Response<>(commandManager.getCommandHistory(13));
        }
    }
}

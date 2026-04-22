package managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commands.Command;
import common.transfer.request.Request;


/**
 * Оперирует списком комманд и историей выполнения.
 * @author Septyq
 */
public class CommandManager implements AbstractCommandManager {
    private final Map<String, Command<?>> commands = new HashMap<>();
    private final List<String> commandHistory = new ArrayList<>();

    public void register(String commandName, Command<?> command) {
        commands.put(commandName, command);
    }

    public Map<String, Command<?>> getCommands() {
        return commands;
    }

    public List<String> getCommandHistory(int number) {
        return commandHistory.subList(0, Math.min(number, commandHistory.size()));
    }

    public Map<String, Class<? extends Request>> getCommandAttributes() {
        Map<String, Class<? extends Request>> commandAttributes = new HashMap<>();
        commands.forEach((name, command) -> {
            commandAttributes.put(name, command.getAttribute().getArgsType());
        });
        return commandAttributes;
    };

    public void addToHistory(String command) {
        commandHistory.add(command);
    }
}

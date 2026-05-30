package commands.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import commands.interfaces.Executable;
import common.command.CommandAttribute;


/**
 * Оперирует списком комманд и историей выполнения.
 * @author Septyq
 */
public class CommandManagerImpl implements CommandManager {
    private final Map<String, Executable> commands = new ConcurrentHashMap<>();
    private final List<String> commandHistory = new ArrayList<>();

    public void register(String commandName, Executable command) {
        commands.put(commandName, command);
    }

    public Map<String, Executable> getCommands() {
        return commands;
    }

    public List<String> getCommandHistory(int number) {
        synchronized (commandHistory) {
            return new ArrayList<>(commandHistory.subList(0, Math.min(number, commandHistory.size())));
        }
    }

    public Map<String, CommandAttribute> getCommandAttributes() {
        Map<String, CommandAttribute> commandAttributes = new ConcurrentHashMap<>();
        commands.forEach((name, command) -> {
            commandAttributes.put(name, command.getAttribute());
        });
        return commandAttributes;
    };

    public void addToHistory(String command) {
        synchronized(commandHistory) {
            commandHistory.add(command);
        }
    }
}

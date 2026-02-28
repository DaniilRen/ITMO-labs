package managers;

import java.util.List;
import java.util.Map;

import commands.Command;

public interface CommandManager {
    void register(String commandName, Command command);
    Map<String, Command> getCommands();
    List<String> getCommandHistory(int number);
    void addToHistory(String command);
}

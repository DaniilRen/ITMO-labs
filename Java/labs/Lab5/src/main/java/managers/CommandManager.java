package managers;

import java.util.List;
import java.util.Map;

import commands.Command;
import util.transfer.request.Request;


/**
 * Абстрактный класс для управления списком комманд и историей выполнения.
 * @author Septyq
 */
public interface CommandManager {
    void register(String commandName, Command<?> command);
    Map<String, Command<?>> getCommands();
    List<String> getCommandHistory(int number);
    Map<String, Class<? extends Request>> getCommandAttributes();
    void addToHistory(String command);
    void putToScriptStack(String script);
    List<String> getScriptStack();
    void clearScriptStack();
    boolean checkRecursion(String currentScript);
}

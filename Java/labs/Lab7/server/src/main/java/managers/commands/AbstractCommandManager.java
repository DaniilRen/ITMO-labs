package managers.commands;

import java.util.List;
import java.util.Map;

import commands.interfaces.Executable;
import common.transfer.request.Request;


/**
 * Абстрактный класс для управления списком комманд и историей выполнения.
 * @author Septyq
 */
public interface AbstractCommandManager {
    void register(String commandName, Executable command);
    Map<String, Executable> getCommands();
    List<String> getCommandHistory(int number);
    Map<String, Class<? extends Request>> getCommandAttributes();
    void addToHistory(String command);
}

package commands;

import java.util.ArrayList;
import java.util.List;

import managers.CommandManager;


public class Help extends Command {
  private final CommandManager commandManager;

  public Help(CommandManager commandManager) {
    super("help", "вывести справку по доступным командам");
    this.commandManager = commandManager;
  }

  public ArrayList<String> execute(String... args) {
    StringBuilder infoText = new StringBuilder();
    commandManager.getCommands().values().forEach(command -> {
      infoText.append(command.getName() + ": " + command.getDescription() + "\n\n");
    });
    return new ArrayList<String>(List.of(infoText.toString()));
  }
}
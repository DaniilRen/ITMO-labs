package commands;

import java.util.List;
import util.Response;
import util.Status;
import managers.CommandManager;


public class Help extends Command {
  private final CommandManager commandManager;

  public Help(CommandManager commandManager) {
    super("help", "вывести справку по доступным командам");
    this.commandManager = commandManager;
  }

  public Response<?> execute(List<?> args) {
    if (args.size() > 0) { return new Response<>(List.of("Invalid argument number"), Status.ERROR); }
    
    StringBuilder infoText = new StringBuilder();
    commandManager.getCommands().values().forEach(command -> {
      infoText.append(command.getName() + ": " + command.getDescription() + "\n\n");
    });
    return new Response<String>(List.of(infoText.toString()));
  }
}
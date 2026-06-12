package commands;

import java.util.List;

import commands.manager.CommandManager;
import common.command.CommandAttribute;
import common.command.PublicityMarker;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;


/**
 * Команда 'help'. Выводит справку по доступным командам.
 * @author Septyq
 */
public class Help extends Command<StandartRequest> {
  private static final long serialVersionUID = 698347L;

  private final CommandManager commandManager;

  public Help(CommandManager commandManager) {
    super(new CommandAttribute(
      "help", 
      "вывести справку по доступным командам",
      StandartRequest.class,
      PublicityMarker.PRIVATE
      ));
    this.commandManager = commandManager;
  }

  public Response<?> execute(StandartRequest request) {
    StringBuilder infoText = new StringBuilder();
    commandManager.getCommands().values().forEach(command -> {
      infoText.append(command.getAttribute().getName() + ": " + command.getAttribute().getDescription() + "\n\n");
    });
    try {
      Thread.sleep(3000);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    return new Response<String>(List.of(infoText.toString()));
  }
}
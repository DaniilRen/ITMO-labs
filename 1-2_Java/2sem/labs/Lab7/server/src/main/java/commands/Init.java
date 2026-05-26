package commands;

import java.util.List;

import commands.manager.CommandManager;
import common.command.CommandAttribute;
import common.command.PublicityMarker;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;


/**
 * Вспомогатльная команда для инициализации клиента. Недоступна пользователям напрямую
 * @author Septyq
 */
public class Init extends Command<StandartRequest> {
  private static final long serialVersionUID = 698347L;

  private final CommandManager commandManager;

  public Init(CommandManager commandManager) {
    super(new CommandAttribute(
      "help", 
      "получение аттрибутов комманд",
      StandartRequest.class,
      PublicityMarker.SERVICE
      ));
    this.commandManager = commandManager;
  }

  public Response<?> execute(StandartRequest request) {
    return new Response<>(List.of(commandManager.getCommandAttributes()));
  }
}


package commands;

import common.transfer.Status;
import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;


/**
 * Команда 'exit'. Завершиет программу (без сохранения в файл).
 * @author Septyq
 */
public class Exit extends Command<StandartRequest> {
    public Exit() {
        super(new CommandAttribute(
            "exit", 
            "завершить программу (без сохранения в файл)", 
            StandartRequest.class
            ));
    }

    public Response<?> execute(StandartRequest request) {
        return new Response<>(Status.EXIT);
    }
}

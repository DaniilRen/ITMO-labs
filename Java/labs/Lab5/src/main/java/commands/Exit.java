package commands;

import util.Response;
import util.Status;

import java.util.List;

/**
 * Команда 'exit'. Завершиет программу (без сохранения в файл).
 * @author Septyq
 */
public class Exit extends Command {
    public Exit() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    public Response<?> execute(List<?> args) {
        if (args.size() != 0) {
            return new Response<>(Status.ERROR);
        }
        return new Response<>(Status.EXIT);
    }
}

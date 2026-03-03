package commands.interfaces;

import util.transfer.Response;
import util.transfer.request.standart.StandartRequest;


/**
 * Определяет классы с возможностью запуска.
 * @author Septyq
 */
public interface Executable<T extends StandartRequest> {
    Response<?> execute(T request);
}

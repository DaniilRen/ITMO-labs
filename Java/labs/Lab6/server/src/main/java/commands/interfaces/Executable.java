package commands.interfaces;

import common.transfer.request.standart.StandartRequest;
import common.transfer.response.Response;


/**
 * Определяет классы с возможностью запуска.
 * @author Septyq
 */
public interface Executable<T extends StandartRequest> {
    Response<?> execute(T request);
}

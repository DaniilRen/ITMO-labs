package util.transfer.request.standart;

import util.transfer.request.Request;


/**
 * Стандартный запрос без аргументов.
 * @author Septyq
 */
public class StandartRequest implements Request {
    private final String name;

    public StandartRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    };
}

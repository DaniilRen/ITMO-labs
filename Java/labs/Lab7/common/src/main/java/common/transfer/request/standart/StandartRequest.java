package common.transfer.request.standart;

import common.transfer.request.Request;


/**
 * Стандартный запрос без аргументов.
 * @author Septyq
 */
public class StandartRequest extends Request {
    private static final long serialVersionUID = 876349874L;

    private final String name;

    public StandartRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    };
}

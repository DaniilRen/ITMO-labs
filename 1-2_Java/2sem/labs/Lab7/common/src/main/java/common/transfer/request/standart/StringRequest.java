package common.transfer.request.standart;

import java.util.List;


/**
 * Запрос со строкой.
 * @author Septyq
 */
public class StringRequest extends StandartRequest {
    private static final long serialVersionUID = 120822749874L;

    private final String row;

    public StringRequest(String name, String row) {
        super(name);
        this.row = row;
    }

    public String getRow() {
        return row;
    }

    public static boolean validate(List<?> args) {
        return (args.size() == 1 && args.get(0) instanceof String && args.get(0) != "");
    }
}

package commands.interfaces;

import java.util.List;
import util.Payload;


/**
 * Определяет классы с возможностью запуска.
 * @author Septyq
 */
public interface Executable {
    Payload<?> execute(List<?> args);
}

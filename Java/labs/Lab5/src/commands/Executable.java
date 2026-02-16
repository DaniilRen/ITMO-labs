package commands;

import java.util.List;
import util.Payload;


public interface Executable {
    Payload<?> execute(List<?> args);
}

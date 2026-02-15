package commands;

import java.util.ArrayList;

public interface Executable {
    ArrayList<?> execute(String... args);
}

package util.execution;

import java.util.List;

@FunctionalInterface
public interface ScriptCommandExecutor {
    void apply(String name, List<?> data, boolean fileMode);
}

package util.execution;

import java.util.List;

import common.transfer.Status;

@FunctionalInterface
public interface ScriptCommandExecutor {
  Status apply(String name, List<?> data, boolean fileMode);
}

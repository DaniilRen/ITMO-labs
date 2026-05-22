package util.execution;

import java.util.List;

import common.transfer.Status;

@FunctionalInterface
public interface StatusExecutor {
    Status apply(String name, List<?> data, boolean isActive);
}

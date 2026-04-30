package commands;

import java.io.Serializable;
import java.util.Objects;

import commands.interfaces.Executable;
import common.transfer.request.Request;
import common.transfer.response.Response;

/**
 * Абстрактная класс для реализации выполнения команды
 * @param <T> тип запроса
 * @author Septyq
 */
public abstract class Command<T extends Request> implements Serializable, Executable {
    private static final long serialVersionUID = 987349874L;

    private final CommandAttribute commandAttribute;

    public Command(CommandAttribute commandAttribute) {
        this.commandAttribute = commandAttribute;
    }

    public CommandAttribute getAttribute() {
        return commandAttribute;
    }

    public abstract Response<?> execute(T request);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command<?> command = (Command<?>) o;
        return Objects.equals(commandAttribute.getName(), command.getAttribute().getName()) 
            && Objects.equals(commandAttribute.getDescription(), command.getAttribute().getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(commandAttribute.getName(), commandAttribute.getDescription());
    }

    @Override
    public String toString() {
        return "Command{" +
        "name='" + commandAttribute.getName() + '\'' +
        ", description='" + commandAttribute.getDescription() + '\'' +
        '}';
    }

}

package commands;


public interface Executable {
    CommandResponse<?> execute(String... args);
}

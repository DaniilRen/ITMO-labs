package logging;

public interface LoggingManager {

	public void info(String message);

	public void error(String message);

	public void error(String message, Throwable throwable);

	public void debug(String message);

	public void warn(String message);

}

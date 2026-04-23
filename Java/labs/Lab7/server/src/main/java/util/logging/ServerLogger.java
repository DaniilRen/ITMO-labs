package util.logging;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerLogger implements AbstractLogger {
	private final Logger log;

	public ServerLogger(String name) {
		this.log = LogManager.getLogger(name);
	}

	public void info(String message) {
			log.info(message);
	}

	public void error(String message) {
			log.error(message);
	}

	public void error(String message, Throwable throwable) {
			log.error(message, throwable);
	}

	public void debug(String message) {
			log.debug(message);
	}

	public void warn(String message) {
			log.warn(message);
	}

}

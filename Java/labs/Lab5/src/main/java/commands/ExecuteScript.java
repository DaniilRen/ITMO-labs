package commands;

import java.util.List;

import runtime.client.LocalRuntime;
import runtime.server.RemoteRuntime;
import util.Response;
import util.Status;

/**
 * Команда 'execute_script'. Исполняет скрипт из указанного файла.
 * @author Septyq
 */
public class ExecuteScript extends Command {
    private final RemoteRuntime remoteRuntime;
    
    public ExecuteScript(RemoteRuntime remoteRuntime) {
        super("execute_script <file_name>", "исполнить скрипт из указанного файла");
        this.remoteRuntime = remoteRuntime;
    }

    public Response<?> execute(List<?> args) {
        if (args.size() == 1) {
            String fileName = (String) args.get(0);
            if (fileName == "") {return new Response<>(List.of("Invalid script name"), Status.ERROR);}

            LocalRuntime localRuntime = new LocalRuntime(remoteRuntime);
            localRuntime.run("script", fileName);

            return new Response<>();
        } else {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        }
    }
}

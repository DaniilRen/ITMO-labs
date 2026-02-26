package commands;

import java.util.List;

import runtime.client.LocalRuntime;
import runtime.server.RemoteRuntime;
import util.Response;
import util.Status;

public class ExecuteScript extends Command {
    
    public ExecuteScript() {
        super("execute_script <file_name>", "исполнить скрипт из указанного файла");
    }

    public Response<?> execute(List<?> args) {
        if (args.size() == 1) {
            String fileName = (String) args.get(0);
            if (fileName == "") {return new Response<>(List.of("Invalid file name"), Status.ERROR);}

            RemoteRuntime remoteRuntime = new RemoteRuntime(fileName);

            LocalRuntime localRuntime = new LocalRuntime(remoteRuntime);
            localRuntime.run("script", fileName);

            return new Response<>();
        } else {
            return new Response<>(List.of("Invalid argument length"), Status.ERROR);
        }
    }
}

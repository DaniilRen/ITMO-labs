package commands;

import java.util.List;

import runtime.LocalRuntime;
import runtime.RemoteRuntime;
import util.Status;
import util.transfer.request.standart.StringRequest;
import util.transfer.response.Response;

/**
 * Команда 'execute_script'. Исполняет скрипт из указанного файла.
 * @author Septyq
 */
public class ExecuteScript extends Command<StringRequest> {
    private final RemoteRuntime remoteRuntime;
    
    public ExecuteScript(RemoteRuntime remoteRuntime) {
        super(new CommandAttribute(
            "execute_script <file_name>", 
            "исполнить скрипт из указанного файла", 
            StringRequest.class
            ));
        this.remoteRuntime = remoteRuntime;
    }

    public Response<?> execute(StringRequest request) {
        String fileName = request.getRow();
        if (fileName == "") {return new Response<>(List.of("Invalid script name"), Status.ERROR);}

        LocalRuntime localRuntime = new LocalRuntime(remoteRuntime);
        localRuntime.run("script", fileName);

        return new Response<>();
    }
}

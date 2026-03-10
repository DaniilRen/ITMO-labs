package commands;

import java.util.List;

import managers.CommandManager;
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
    private final CommandManager commandManager;
    
    public ExecuteScript(RemoteRuntime remoteRuntime, CommandManager commandManager) {
        super(new CommandAttribute(
            "execute_script <file_name>", 
            "исполнить скрипт из указанного файла", 
            StringRequest.class
            ));
        this.remoteRuntime = remoteRuntime;
        this.commandManager = commandManager;
    }

    public Response<?> execute(StringRequest request) {
        String fileName = request.getRow();
        if (fileName == "") return new Response<>(List.of("Invalid script name"), Status.ERROR);

        commandManager.pushScript(fileName);

        LocalRuntime localRuntime = new LocalRuntime(remoteRuntime);
        localRuntime.run("script", fileName);

        commandManager.popScript(fileName);

        return new Response<>();
    }
}

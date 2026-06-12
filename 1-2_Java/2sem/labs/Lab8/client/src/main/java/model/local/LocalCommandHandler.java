package model.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.local.localActions.LocalAction;
import model.local.localActions.ScriptAction;
import model.local.script.ScriptProcessor;
import view.View;

public class LocalCommandHandler {
    private List<String> localCommands;
    private final View view;
    private ScriptProcessor scriptProcessor;
    private Map<String, LocalAction> localCommandActions;

    public LocalCommandHandler(View view, ScriptProcessor scriptProcessor) {
        this.view = view;
        this.scriptProcessor = scriptProcessor;
        this.localCommands = new ArrayList<>(Arrays.asList("logout", "exit", "execute_script"));
    }

    public boolean isLocalCommand(String name) {
        return localCommands.contains(name);
    }

    public void setLocalCommands(Map<String, LocalAction> localCommandActions) {
        this.localCommandActions = localCommandActions;
    }

    public void handleLocalCommand(String name, List<?> args) {
        if (!(isLocalCommand(name))) {
            view.displayError("Unknown local command");
        }
        LocalAction action = localCommandActions.get(name);
        applyLocalAction(action, args);
    }

    private void applyLocalAction(LocalAction action, List<?> args) {
        if (action instanceof ScriptAction) {
            ScriptAction scriptAction = (ScriptAction) action;
            scriptAction.apply(view, scriptProcessor, args);
            return;
        }
        action.apply(view);
    }

}

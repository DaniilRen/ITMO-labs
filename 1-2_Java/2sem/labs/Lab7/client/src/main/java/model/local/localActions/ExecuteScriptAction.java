package model.local.localActions;

import java.util.List;

import common.exceptions.InvalidScriptException;
import model.local.script.ScriptProcessor;
import view.View;

public class ExecuteScriptAction implements ScriptAction {
    public void apply(View view, ScriptProcessor scriptProcessor, List<?> args) {
        if (args.isEmpty()) {
            view.displayError("No script file specified");
            return;
        }
        try {
            scriptProcessor.executeScript((String) args.get(0));
        } catch (InvalidScriptException | ClassCastException e) {
            view.displayError(e.getMessage());
        }
    }

    public void apply(View view) {
        view.displayError("No script file specified");
    }
}

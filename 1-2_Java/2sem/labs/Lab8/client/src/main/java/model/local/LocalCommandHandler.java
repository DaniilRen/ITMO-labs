package model.local;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import common.transfer.Status;
import model.local.localActions.ExecuteScriptAction;
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

  public Status handleLocalCommand(String name, List<?> args) {
    if (!isLocalCommand(name)) {
      view.displayError("Unknown local command");
      return Status.ERROR;
    }
    LocalAction action = localCommandActions.get(name);
    return applyLocalAction(action, args);
  }

  private Status applyLocalAction(LocalAction action, List<?> args) {
    if (action instanceof ExecuteScriptAction scriptAction) {
      return scriptAction.applyWithStatus(view, scriptProcessor, args);
    }
    if (action instanceof ScriptAction scriptAction) {
      scriptAction.apply(view, scriptProcessor, args);
      return Status.OK;
    }
    action.apply(view);
    return Status.OK;
  }
}

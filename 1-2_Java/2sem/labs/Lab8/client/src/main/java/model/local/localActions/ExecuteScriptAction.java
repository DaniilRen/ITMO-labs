package model.local.localActions;

import java.util.List;

import common.exceptions.InvalidScriptException;
import common.transfer.Status;
import model.local.script.ScriptProcessor;
import view.View;

public class ExecuteScriptAction implements ScriptAction {
  @Override
  public void apply(View view, ScriptProcessor scriptProcessor, List<?> args) {
    applyWithStatus(view, scriptProcessor, args);
  }

  public Status applyWithStatus(View view, ScriptProcessor scriptProcessor, List<?> args) {
    if (args.isEmpty()) {
      view.displayError("No script file specified");
      return Status.ERROR;
    }
    try {
      scriptProcessor.executeScript(String.valueOf(args.get(0)));
      return Status.OK;
    } catch (InvalidScriptException | ClassCastException e) {
      if (view instanceof view.gui.GraphicsView guiView) {
        guiView.setLastError(e.getMessage());
      } else {
        view.displayError(e.getMessage());
      }
      return Status.ERROR;
    }
  }

  public void apply(View view) {
    view.displayError("No script file specified");
  }
}

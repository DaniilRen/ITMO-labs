package model.local.localActions;

import java.util.List;

import model.local.script.ScriptProcessor;
import view.View;

public interface ScriptAction extends LocalAction{
    public void apply(View view, ScriptProcessor scriptProcessor, List<?> args);
}

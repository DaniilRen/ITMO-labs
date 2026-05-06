package util.controller;

import java.util.HashSet;
import java.util.Set;

public class DefaultRecursionController implements RecursionController {
    private Set<String> scriptStack = new HashSet<>();

    public void pushScript(String script) {
        scriptStack.add(script);
    }

    public void popScript(String script) {
        scriptStack.remove(script);
    }

    public boolean checkRecursion(String script) {
        if (scriptStack.contains(script)) {
            scriptStack.clear();
            return true;
        }
        return false;
    }
}

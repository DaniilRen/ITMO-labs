package util.script;

import java.util.HashSet;
import java.util.Set;

public class RecursionController {
    private static Set<String> scriptStack = new HashSet<>();

    public static void pushScript(String script) {
        scriptStack.add(script);
    }

    public static void popScript(String script) {
        scriptStack.remove(script);
    }

    public static boolean checkRecursion(String script) {
        if (scriptStack.contains(script)) {
            scriptStack.clear();
            return true;
        }
        return false;
    }
}

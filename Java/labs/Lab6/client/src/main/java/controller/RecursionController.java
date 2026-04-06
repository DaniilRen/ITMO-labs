package controller;

public interface RecursionController {
    void pushScript(String script);
    void popScript(String script);
    boolean checkRecursion(String script);
}

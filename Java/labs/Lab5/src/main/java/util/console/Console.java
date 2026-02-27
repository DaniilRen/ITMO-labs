package util.console;


public interface Console {
    void print(Object obj);
    void println(Object obj);
    void printError(Object obj);
    void printPromptSymbol();
    String getPromptSymbol();
}
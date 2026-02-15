package util.console;

public class DefaultConsole implements Console {
    public void print(Object obj) {
        System.out.print(obj);
    }

    public void println(Object obj) {
        System.out.println(obj);
    }

    public void printError(Object obj) {
        System.out.println("[Error]: " + obj);
    }
}

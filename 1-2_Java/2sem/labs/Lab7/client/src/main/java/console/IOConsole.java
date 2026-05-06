package console;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;


/**
 * Консоль воода-вывода.
 * @author Septyq
 */
public class IOConsole implements Console {
    private Scanner userScanner;
    private boolean fileMode = false;
    private final String promptSymbol = "$ ";
    private final String scriptPrompotySymbol = ":> ";
		private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    public Scanner getUserScanner() {
        return this.userScanner;
    }

    public void setUserScanner(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public boolean fileMode() {
        return fileMode;
    }

    public void setUserMode() {
        this.fileMode = false;
    }

    public void setFileMode() {
        this.fileMode = true;
    }

    public void print(Object obj) {
        out.print(obj.toString());
    }

    public void println(Object obj) {
        out.println(obj.toString());
    }

    public void printError(Object obj) {
        out.println("[Error]: " + obj.toString());
    }

    public void printConnectionError(Object obj) {
        out.println("[Connection Error]: " + obj.toString());
    }

    public void printPromptSymbol() {
        print(promptSymbol);
    }

    public String getPromptSymbol() {
        return promptSymbol;
    }

    public String getScriptPromptSymbol() {
        return scriptPrompotySymbol;
    }
}

package util.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.BiFunction;

import common.exceptions.InvalidScriptException;
import common.transfer.Status;
import console.IOConsole;


public class ScriptProcessor {
    private final List<String> scriptStack = new ArrayList<>();
    private final IOConsole console;
    private final BiFunction<String, List<?>, Status> executeMethod;

    public ScriptProcessor(IOConsole console, BiFunction<String, List<?>, Status> executeMethod) {
        this.console = console;
        this.executeMethod = executeMethod;
    }

    public void executeScript(String fileName) throws InvalidScriptException {
        try {
            validateScript(fileName);   
        } catch (InvalidScriptException e) { throw e; }

        RecursionHandler.pushScript(fileName);

        Status commandStatus = Status.OK;
        scriptStack.add(fileName);

        Scanner oldScanner = console.getUserScanner();
        
        try (Scanner scriptScanner = new Scanner(new File(fileName))) {
            console.println(String.format("--> RUNNING SCRIPT: %s ...", fileName));

            File file = new File(fileName);
            if (!file.exists()) throw new FileNotFoundException("File does not exist");
            if (!file.canRead()) throw new SecurityException("No read permission for file: " + file.getAbsolutePath());
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();

            console.setUserScanner(scriptScanner);
            console.setFileMode();

            while (commandStatus != Status.EXIT && scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                
                while (line.isEmpty() && scriptScanner.hasNextLine()) {
                    line = scriptScanner.nextLine().trim();
                }
                if (line.isEmpty()) continue;
                
                console.println(console.getScriptPromptSymbol() + line);

                String[] parts = line.split(" ", 2);
                String commandName = parts[0];
                String arg = parts.length > 1 ? parts[1] : "";
                
                List<?> args = arg.isEmpty() ? List.of() : List.of(arg);
                
                commandStatus = executeMethod.apply(commandName, args);
            }

        } catch (FileNotFoundException exception) {
            console.printError("File not found");
        } catch (NoSuchElementException exception) {
            console.printError("File is empty");
        } catch (IllegalStateException exception) {
            console.printError("Unknown error");
            System.exit(0);
        } catch (SecurityException e) {
            console.printError(e.getMessage());
        } finally {
            console.setUserScanner(oldScanner);
            console.setUserMode();
            scriptStack.remove(scriptStack.size() - 1);
            RecursionHandler.popScript(fileName);
        }
    }

    private void validateScript(String fileName) throws InvalidScriptException {
        if (fileName == null || fileName.isEmpty()) {
            throw new InvalidScriptException("Script name is empty");
        }
        if (RecursionHandler.checkRecursion(fileName)) {
            throw new InvalidScriptException("Script has recursion");
        }
    }
}

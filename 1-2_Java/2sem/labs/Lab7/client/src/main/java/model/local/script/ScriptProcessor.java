package model.local.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import common.exceptions.InvalidScriptException;
import common.models.Entity;
import common.models.User;
import util.forms.buidler.ScriptFormBuidler;
import util.execution.ScriptCommandExecutor;
import view.View;


public class ScriptProcessor {
    private final List<String> scriptStack = new ArrayList<>();
    private final View view;
    private final ScriptCommandExecutor executeMethod;
    private Scanner scriptScanner;
    private boolean running = false;

    public ScriptProcessor(View view) {
        this.view = view;
        this.executeMethod = view::executeCommand;
    }

    public void setScanner(Scanner scanner) {
        this.scriptScanner = scanner;
    }

    public Scanner getScanner() {
        return scriptScanner;
    }

    public Entity onEntityAdd(String author) {
        Scanner scanner = getScanner();
        if (scanner == null) {
            throw new IllegalStateException("Scanner is not initialized");
        }
        ScriptFormBuidler builder = new ScriptFormBuidler(scanner);
        return builder.buildEntity(author);
    }

    public User onLogin() {
        Scanner scanner = getScanner();
        if (scanner == null) {
            throw new IllegalStateException("Scanner is not initialized");
        }
        ScriptFormBuidler builder = new ScriptFormBuidler(scanner);
        return builder.buildUser(false);
    }

    public User onRegister() {
        Scanner scanner = getScanner();
        if (scanner == null) {
            throw new IllegalStateException("Scanner is not initialized");
        }
        ScriptFormBuidler builder = new ScriptFormBuidler(scanner);
        return builder.buildUser(true);
    }

    public void executeScript(String fileName) throws InvalidScriptException {
        try {
            validateScript(fileName);   
        } catch (InvalidScriptException e) { throw e; }

        RecursionHandler.pushScript(fileName);

        scriptStack.add(fileName);
        
        try {
            setScanner(new Scanner(new File(fileName)));

            view.displayMessage(String.format("--> RUNNING SCRIPT: %s ...", fileName));

            File file = new File(fileName);
            if (!file.exists()) throw new FileNotFoundException("File does not exist");
            if (!file.canRead()) throw new SecurityException("No read permission for file: " + file.getAbsolutePath());
            if (!scriptScanner.hasNext()) throw new NoSuchElementException();

            this.running = true;

            while (this.running && scriptScanner.hasNextLine()) {
                String line = scriptScanner.nextLine().trim();
                
                while (line.isEmpty() && scriptScanner.hasNextLine()) {
                    line = scriptScanner.nextLine().trim();
                }
                if (line.isEmpty()) continue;
            
                String[] parts = line.split(" ", 2);
                String commandName = parts[0];
                String arg = parts.length > 1 ? parts[1] : "";
                
                List<?> args = arg.isEmpty() ? List.of() : List.of(arg);
                
                executeMethod.apply(commandName, args, true);
            }

        } catch (FileNotFoundException exception) {
            view.displayMessage("File not found");
        } catch (NoSuchElementException exception) {
            view.displayMessage("File is empty");
        } catch (IllegalStateException exception) {
            view.displayMessage("Unknown error");
            System.exit(0);
        } catch (SecurityException e) {
            view.displayMessage(e.getMessage());
        } finally {
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

    public void stopScript() {
        view.displayMessage("Script stopped");
        this.running = false;
    }
}

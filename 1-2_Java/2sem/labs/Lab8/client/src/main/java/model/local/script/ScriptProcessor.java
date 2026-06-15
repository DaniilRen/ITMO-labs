package model.local.script;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import common.exceptions.InvalidScriptException;
import common.models.Entity;
import common.models.User;
import common.transfer.Status;
import util.execution.ScriptCommandExecutor;
import util.forms.buidler.ScriptFormBuidler;
import util.script.ScriptPaths;
import view.View;

public class ScriptProcessor {
  private final List<String> scriptStack = new ArrayList<>();
  private final View view;
  private final ScriptCommandExecutor executeMethod;
  private Scanner scriptScanner;
  private boolean running = false;

  public ScriptProcessor(View view, ScriptCommandExecutor executeMethod) {
    this.view = view;
    this.executeMethod = executeMethod;
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
    String resolvedPath = ScriptPaths.resolve(fileName);
    validateScript(resolvedPath);

    RecursionHandler.pushScript(resolvedPath);
    scriptStack.add(resolvedPath);
    view.setScriptQuietMode(true);

    File file = new File(resolvedPath);
    if (!file.isFile() || !file.canRead()) {
      finishScript(resolvedPath);
      throw new InvalidScriptException("File not found: " + resolvedPath);
    }

    try (Scanner scanner = new Scanner(file)) {
      if (!scanner.hasNextLine()) {
        throw new InvalidScriptException("File is empty: " + resolvedPath);
      }

      setScanner(scanner);
      this.running = true;

      while (this.running && scanner.hasNextLine()) {
        String line = scanner.nextLine().trim();

        while (line.isEmpty() && scanner.hasNextLine()) {
          line = scanner.nextLine().trim();
        }
        if (line.isEmpty()) {
          continue;
        }

        String[] parts = line.split(" ", 2);
        String commandName = parts[0];
        String arg = parts.length > 1 ? parts[1] : "";

        List<?> args = arg.isEmpty() ? List.of() : List.of(arg);

        Status status = executeMethod.apply(commandName, args, true);
        if (status == Status.ERROR) {
          throw new InvalidScriptException("Script stopped: command failed");
        }
        if ("exit".equals(commandName)) {
          break;
        }
      }
    } catch (FileNotFoundException e) {
      throw new InvalidScriptException("File not found: " + resolvedPath);
    } finally {
      finishScript(resolvedPath);
    }
  }

  private void finishScript(String resolvedPath) {
    setScanner(null);
    this.running = false;
    view.setScriptQuietMode(false);
    if (!scriptStack.isEmpty()) {
      scriptStack.remove(scriptStack.size() - 1);
    }
    RecursionHandler.popScript(resolvedPath);
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
    this.running = false;
  }
}

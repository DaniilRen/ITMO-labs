package util.script;

import java.nio.file.Files;
import java.nio.file.Path;

import common.exceptions.InvalidScriptException;

public final class ScriptPaths {
  private static final String DATA_DIR = "data";

  private ScriptPaths() {}

  public static String resolve(String input) throws InvalidScriptException {
    if (input == null || input.isBlank()) {
      throw new InvalidScriptException("Script name is empty");
    }

    String normalized = input.trim().replace('\\', '/');
    if (normalized.contains("..")) {
      throw new InvalidScriptException("Invalid script path");
    }

    Path candidate =
        normalized.startsWith(DATA_DIR + "/")
            ? Path.of(normalized)
            : Path.of(DATA_DIR, normalized);

    if (Files.isRegularFile(candidate)) {
      return candidate.toString().replace('\\', '/');
    }

    if (!normalized.endsWith(".script")) {
      Path withExtension =
          normalized.startsWith(DATA_DIR + "/")
              ? Path.of(normalized + ".script")
              : Path.of(DATA_DIR, normalized + ".script");
      if (Files.isRegularFile(withExtension)) {
        return withExtension.toString().replace('\\', '/');
      }
    }

    throw new InvalidScriptException("File not found: " + candidate);
  }
}

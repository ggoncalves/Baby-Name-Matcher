package com.ggoncalves.babynamematcher.validator;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathValidator {

  public static boolean isValidExistingFilePath(String filePath) {
    if (filePath == null || filePath.trim().isEmpty()) {
      return false;
    }

    try {
      Path path = Paths.get(filePath);
      return Files.exists(path);
    } catch (Exception e) {
      // Invalid path syntax
      return false;
    }
  }

  public static boolean isValidPathSyntax(String filePath) {
    if (filePath == null || filePath.trim().isEmpty()) {
      return false;
    }

    try {
      Paths.get(filePath);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public static ValidationResult validateFilePath(String filePath) {
    ValidationResult result = new ValidationResult();

    if (filePath == null || filePath.trim().isEmpty()) {
      result.setValid(false);
      result.setErrorMessage("Path is null or empty");
      return result;
    }

    try {
      Path path = Paths.get(filePath);
      File file = path.toFile();

      result.setValid(true);
      result.setExists(Files.exists(path));

      if (result.isExists()) {
        result.setDirectory(Files.isDirectory(path));
        result.setReadable(file.canRead());
        result.setWritable(file.canWrite());
        result.setExecutable(file.canExecute());
      }

    } catch (Exception e) {
      result.setValid(false);
      result.setErrorMessage("Invalid path syntax: " + e.getMessage());
    }

    return result;
  }

  /**
   * Result class to hold validation details
   */
  public static class ValidationResult {
    private boolean valid;
    private boolean exists;
    private boolean isDirectory;
    private boolean readable;
    private boolean writable;
    private boolean executable;
    private String errorMessage;

    // Getters and setters
    public boolean isValid() { return valid; }
    public void setValid(boolean valid) { this.valid = valid; }

    public boolean isExists() { return exists; }
    public void setExists(boolean exists) { this.exists = exists; }

    public boolean isDirectory() { return isDirectory; }
    public void setDirectory(boolean directory) { isDirectory = directory; }

    public boolean isReadable() { return readable; }
    public void setReadable(boolean readable) { this.readable = readable; }

    public boolean isWritable() { return writable; }
    public void setWritable(boolean writable) { this.writable = writable; }

    public boolean isExecutable() { return executable; }
    public void setExecutable(boolean executable) { this.executable = executable; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
  }
}

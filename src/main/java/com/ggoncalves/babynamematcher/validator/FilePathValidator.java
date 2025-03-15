package com.ggoncalves.babynamematcher.validator;

import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FilePathValidator {

  public static boolean isValidExistingFilePath(String filePath) {
    if (isEmptyOrNullFilePath(filePath)) return false;

    try {
      return Files.exists(Paths.get(filePath));
    }
    catch (Exception e) {
      // Invalid path syntax
      return false;
    }
  }

  public static boolean isValidPathSyntax(String filePath) {
    if (isEmptyOrNullFilePath(filePath)) return false;

    try {
      Paths.get(filePath);
      return true;
    }
    catch (Exception e) {
      return false;
    }
  }

  public static ValidationResult validateFilePath(String filePath) {

    if (filePath == null || filePath.trim().isEmpty()) {
      return ValidationResult.builder()
          .valid(false)
          .errorMessage("Path is null or empty")
          .build();
    }

    ValidationResult.ValidationResultBuilder validationResultBuilder = ValidationResult.builder();

    try {
      Path path = Paths.get(filePath);
      File file = path.toFile();

      validationResultBuilder
          .valid(true)
          .exists(Files.exists(path));

      if (validationResultBuilder.exists) {
        validationResultBuilder
            .isDirectory(Files.isDirectory(path))
            .readable(file.canRead())
            .writable(file.canWrite())
            .executable(file.canExecute());
      }

    }
    catch (Exception e) {
      validationResultBuilder
          .valid(false)
          .errorMessage("Invalid path syntax: " + e.getMessage());
    }

    return validationResultBuilder.build();
  }

  private static boolean isEmptyOrNullFilePath(String filePath) {
    return filePath == null || filePath.trim().isEmpty();
  }

  @Data
  @Builder
  public static class ValidationResult {
    private boolean valid;
    private boolean exists;
    private boolean isDirectory;
    private boolean readable;
    private boolean writable;
    private boolean executable;
    private String errorMessage;
  }
}

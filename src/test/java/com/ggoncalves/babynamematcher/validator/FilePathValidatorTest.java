package com.ggoncalves.babynamematcher.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class FilePathValidatorTest {

  @TempDir
  private Path tempDir;

  private Path existingFile;
  private Path nonExistentFile;
  private Path existingDirectory;
  private Path readOnlyFile;

  @BeforeEach
  void setUp() throws IOException {
    // Create test files and directories
    existingDirectory = tempDir;
    existingFile = tempDir.resolve("test-file.txt");
    nonExistentFile = tempDir.resolve("non-existent-file.txt");
    readOnlyFile = tempDir.resolve("read-only-file.txt");

    // Create the files
    Files.createFile(existingFile);
    Files.createFile(readOnlyFile);

    // Make read-only file actually read-only
    File readOnlyFileObj = readOnlyFile.toFile();

    //noinspection ResultOfMethodCallIgnored (this being a test, we just want to set regardless of success)
    readOnlyFileObj.setWritable(false);
  }

  @Nested
  @DisplayName("Tests for isValidExistingFilePath")
  class IsValidExistingFilePathTests {

    @Test
    @DisplayName("Should return false for null path")
    void shouldReturnFalseForNullPath() {
      assertThat(FilePathValidator.isValidExistingFilePath(null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Should return false for empty path")
    void shouldReturnFalseForEmptyPath(String path) {
      assertThat(FilePathValidator.isValidExistingFilePath(path)).isFalse();
    }

    @Test
    @DisplayName("Should return true for existing file")
    void shouldReturnTrueForExistingFile() {
      assertThat(FilePathValidator.isValidExistingFilePath(existingFile.toString())).isTrue();
    }

    @Test
    @DisplayName("Should return true for existing directory")
    void shouldReturnTrueForExistingDirectory() {
      assertThat(FilePathValidator.isValidExistingFilePath(existingDirectory.toString())).isTrue();
    }

    @Test
    @DisplayName("Should return false for non-existent file")
    void shouldReturnFalseForNonExistentFile() {
      assertThat(FilePathValidator.isValidExistingFilePath(nonExistentFile.toString())).isFalse();
    }

    @Test
    @DisplayName("Should return false for invalid path syntax")
    void shouldReturnFalseForInvalidPathSyntax() {
      // On Windows, colons are invalid in file names except for drive letter
      // On Unix-like systems, null character is invalid
      String invalidPath = System.getProperty("os.name").toLowerCase().contains("win")
          ? "C:\\invalid\\path\\with:illegal:character"
          : "/invalid/path/with/\0nullcharacter";

      assertThat(FilePathValidator.isValidExistingFilePath(invalidPath)).isFalse();
    }

    @Test
    @DisplayName("Should handle exceptions thrown by Paths.get")
    void shouldHandleExceptionsFromPathsGet() {
      try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class)) {
        mockedPaths.when(() -> Paths.get(any(String.class)))
            .thenThrow(new RuntimeException("Mocked exception"));

        assertThat(FilePathValidator.isValidExistingFilePath("/some/path")).isFalse();
      }
    }
  }

  @Nested
  @DisplayName("Tests for isValidPathSyntax")
  class IsValidPathSyntaxTests {

    @Test
    @DisplayName("Should return false for null path")
    void shouldReturnFalseForNullPath() {
      assertThat(FilePathValidator.isValidPathSyntax(null)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Should return false for empty paths")
    void shouldReturnFalseForEmptyPaths(String path) {
      assertThat(FilePathValidator.isValidPathSyntax(path)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/valid/path", "C:\\valid\\path", "./relative/path", "../parent/path"})
    @DisplayName("Should return true for valid path syntax")
    void shouldReturnTrueForValidPathSyntax(String path) {
      assertThat(FilePathValidator.isValidPathSyntax(path)).isTrue();
    }

    @Test
    @DisplayName("Should handle exceptions thrown by Paths.get")
    void shouldHandleExceptionsFromPathsGet() {
      try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class)) {
        mockedPaths.when(() -> Paths.get(any(String.class)))
            .thenThrow(new RuntimeException("Mocked exception"));

        assertThat(FilePathValidator.isValidPathSyntax("/some/path")).isFalse();
      }
    }
  }

  @Nested
  @DisplayName("Tests for validateFilePath")
  class ValidateFilePathTests {

    @Test
    @DisplayName("Should return invalid result for null path")
    void shouldReturnInvalidResultForNullPath() {
      FilePathValidator.ValidationResult result = FilePathValidator.validateFilePath(null);

      assertThat(result.isValid()).isFalse();
      assertThat(result.getErrorMessage()).isNotEmpty();
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "   "})
    @DisplayName("Should return invalid result for empty paths")
    void shouldReturnInvalidResultForEmptyPaths(String path) {
      FilePathValidator.ValidationResult result = FilePathValidator.validateFilePath(path);

      assertThat(result.isValid()).isFalse();
      assertThat(result.getErrorMessage()).isNotEmpty();
    }

    @Test
    @DisplayName("Should return valid result with correct attributes for existing file")
    void shouldReturnValidResultWithCorrectAttributesForExistingFile() {
      FilePathValidator.ValidationResult result = FilePathValidator.validateFilePath(existingFile.toString());

      assertThat(result.isValid()).isTrue();
      assertThat(result.isExists()).isTrue();
      assertThat(result.isDirectory()).isFalse();
      assertThat(result.isReadable()).isTrue();
      assertThat(result.isWritable()).isTrue();
      assertThat(result.getErrorMessage()).isNull();
    }

    @Test
    @DisplayName("Should return valid result with correct attributes for existing directory")
    void shouldReturnValidResultWithCorrectAttributesForExistingDirectory() {
      FilePathValidator.ValidationResult result = FilePathValidator.validateFilePath(existingDirectory.toString());

      assertThat(result.isValid()).isTrue();
      assertThat(result.isExists()).isTrue();
      assertThat(result.isDirectory()).isTrue();
      assertThat(result.getErrorMessage()).isNull();
    }

    @Test
    @DisplayName("Should return valid result with exists=false for non-existent file")
    void shouldReturnValidResultWithExistsFalseForNonExistentFile() {
      FilePathValidator.ValidationResult result = FilePathValidator.validateFilePath(nonExistentFile.toString());

      assertThat(result.isValid()).isTrue();
      assertThat(result.isExists()).isFalse();
      assertThat(result.getErrorMessage()).isNull();
    }

    @Test
    @DisplayName("Should return correct writable attribute for read-only file")
    void shouldReturnCorrectWritableAttributeForReadOnlyFile() {
      FilePathValidator.ValidationResult result = FilePathValidator.validateFilePath(readOnlyFile.toString());

      assertThat(result.isValid()).isTrue();
      assertThat(result.isExists()).isTrue();
      assertThat(result.isWritable()).isFalse();
      assertThat(result.isReadable()).isTrue();
    }

    @Test
    @DisplayName("Should handle exceptions thrown during validation")
    void shouldHandleExceptionsDuringValidation() {
      try (MockedStatic<Paths> mockedPaths = mockStatic(Paths.class)) {
        mockedPaths.when(() -> Paths.get(any(String.class)))
            .thenThrow(new RuntimeException("Mocked exception"));

        FilePathValidator.ValidationResult result = FilePathValidator.validateFilePath("/some/path");

        assertThat(result.isValid()).isFalse();
        assertThat(result.getErrorMessage()).contains("Mocked exception");
      }
    }
  }

  @Nested
  @DisplayName("Tests for ValidationResult class")
  class ValidationResultTests {

    @Test
    @DisplayName("Should correctly use getters and setters")
    void shouldCorrectlyUseGettersAndSetters() {
      FilePathValidator.ValidationResult result = new FilePathValidator.ValidationResult();

      result.setValid(true);
      result.setExists(true);
      result.setDirectory(true);
      result.setReadable(true);
      result.setWritable(true);
      result.setExecutable(true);
      result.setErrorMessage("Test error");

      assertThat(result.isValid()).isTrue();
      assertThat(result.isExists()).isTrue();
      assertThat(result.isDirectory()).isTrue();
      assertThat(result.isReadable()).isTrue();
      assertThat(result.isWritable()).isTrue();
      assertThat(result.isExecutable()).isTrue();
      assertThat(result.getErrorMessage()).isEqualTo("Test error");
    }
  }
}
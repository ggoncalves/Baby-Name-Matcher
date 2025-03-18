package com.ggoncalves.babynamematcher.main;

import com.ggoncalves.babynamematcher.BabyNameMatcherApp;
import com.ggoncalves.babynamematcher.exception.ExceptionHandler;
import com.ggoncalves.babynamematcher.exception.FilePermissionException;
import com.ggoncalves.babynamematcher.exception.InvalidFileException;
import com.ggoncalves.babynamematcher.exception.NotEnoughNameListsException;
import com.ggoncalves.babynamematcher.validator.FilePathValidator;
import com.ggoncalves.babynamematcher.validator.ValidationResult;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BabyNameMatcherMainTest {

  @Mock
  private FilePathValidator filePathValidator;

  @Mock
  private ExceptionHandler exceptionHandler;

  @Mock
  private BabyNameMatcherApp babyNameMatcherApp;

  @InjectMocks
  private BabyNameMatcherMain babyNameMatcherMain;

  @Test
  @DisplayName("Should show error asking for at least two lists")
  void shouldShowErrorAskForAtLeastTwoLists() {
    // Arrange
    String[] paths = {"valid_file1.txt"};

    babyNameMatcherMain = createBabyNameMatcherMain(paths);

    // Act & Assert
    babyNameMatcherMain.run();

    // Verify each path was validated
    verifyNoInteractions(filePathValidator);
    verify(exceptionHandler).handle(any(NotEnoughNameListsException.class));
    verify(exceptionHandler).handle(argThat(e -> e.getMessage()
        .contains("At least two name lists are required for matching")));
  }

  @NotNull
  private BabyNameMatcherMain createBabyNameMatcherMain(String[] paths) {
    return new BabyNameMatcherMain(paths, exceptionHandler, filePathValidator, babyNameMatcherApp);
  }

  @Test
  @DisplayName("Should process valid file paths without throwing exceptions")
  void shouldProcessValidFilePaths() {
    // Arrange
    String[] paths = {"valid_file1.txt", "valid_file2.txt"};

    ValidationResult validResult1 = createValidResultForFile("valid_file1.txt");
    ValidationResult validResult2 = createValidResultForFile("valid_file2.txt");

    when(filePathValidator.validateFilePath("valid_file1.txt")).thenReturn(validResult1);
    when(filePathValidator.validateFilePath("valid_file2.txt")).thenReturn(validResult2);

    babyNameMatcherMain = createBabyNameMatcherMain(paths);

    // Act & Assert
    assertThatCode(() -> babyNameMatcherMain.run()).doesNotThrowAnyException();

    // Verify each path was validated
    verify(filePathValidator).validateFilePath("valid_file1.txt");
    verify(filePathValidator).validateFilePath("valid_file2.txt");
    verifyNoMoreInteractions(filePathValidator);
    verifyNoInteractions(exceptionHandler);
  }

  private static ValidationResult createValidResultForFile(String filePath) {
    return ValidationResult.builder()
        .filePath(filePath)
        .valid(true)
        .exists(true)
        .isDirectory(false)
        .readable(true)
        .writable(true)
        .executable(false)
        .build();
  }

  @Test
  @DisplayName("Should throw InvalidFileException when file path is invalid")
  void shouldThrowInvalidFileExceptionWhenFilePathIsInvalid() {
    // Arrange
    String[] paths = {"invalid_file.txt", "valid_file.txt"};

    ValidationResult invalidResult = ValidationResult.builder()
        .filePath("invalid_file.txt")
        .valid(false)
        .exists(false)
        .isDirectory(false)
        .readable(false)
        .writable(false)
        .executable(false)
        .errorMessage("File does not exist")
        .build();

    when(filePathValidator.validateFilePath("invalid_file.txt")).thenReturn(invalidResult);

    babyNameMatcherMain = createBabyNameMatcherMain(paths);

    // Act & Assert
    babyNameMatcherMain.run();

    verify(filePathValidator).validateFilePath("invalid_file.txt");
    verify(exceptionHandler).handle(any(InvalidFileException.class));
    verify(exceptionHandler).handle(argThat(e -> e.getMessage()
        .contains("File does not exist")));
  }

  @Test
  @DisplayName("Should throw InvalidFileException when file is a directory")
  void shouldThrowInvalidFileExceptionWhenFileIsDirectory() {
    // Arrange
    String[] paths = {"directory_path", "valid_file.txt"};

    ValidationResult directoryResult = ValidationResult.builder()
        .filePath("directory_path")
        .valid(true)
        .exists(true)
        .isDirectory(true)
        .readable(true)
        .writable(true)
        .executable(true)
        .errorMessage(null)
        .build();

    when(filePathValidator.validateFilePath("directory_path")).thenReturn(directoryResult);

    babyNameMatcherMain = createBabyNameMatcherMain(paths);

    // Act & Assert
    babyNameMatcherMain.run();

    verify(filePathValidator).validateFilePath("directory_path");
    verify(exceptionHandler).handle(any(InvalidFileException.class));
    verify(exceptionHandler).handle(argThat(e -> e.getMessage()
        .contains("File is a directory")));
  }

  @Test
  @DisplayName("Should throw FilePermissionException when file is unreadable")
  void shouldThrowFilePermissionExceptionWhenFileIsUnreadable() {
    // Arrange
    String[] paths = {"unreadable_file.txt", "valid_file.txt"};

    ValidationResult unreadableResult = ValidationResult.builder()
        .filePath("unreadable_file.txt")
        .valid(true)
        .exists(true)
        .isDirectory(false)
        .readable(false)
        .writable(true)
        .executable(false)
        .errorMessage(null)
        .build();

    when(filePathValidator.validateFilePath("unreadable_file.txt")).thenReturn(unreadableResult);

    babyNameMatcherMain = createBabyNameMatcherMain(paths);

    // Act & Assert
    babyNameMatcherMain.run();

    verify(filePathValidator).validateFilePath("unreadable_file.txt");
    verify(exceptionHandler).handle(any(FilePermissionException.class));
    verify(exceptionHandler).handle(argThat(e -> e.getMessage()
        .contains("Cannot read file")));
  }

  @Test
  @DisplayName("Should process multiple file paths and stop on first error")
  void shouldProcessMultipleFilePathsAndStopOnFirstError() {
    // Arrange
    String[] paths = {"valid_file.txt", "directory_path", "should_not_reach.txt"};

    ValidationResult validResult = ValidationResult.builder()
        .filePath("valid_file.txt")
        .valid(true)
        .exists(true)
        .isDirectory(false)
        .readable(true)
        .writable(true)
        .executable(false)
        .errorMessage(null)
        .build();

    ValidationResult directoryResult = ValidationResult.builder()
        .filePath("directory_path")
        .valid(true)
        .exists(true)
        .isDirectory(true)
        .readable(true)
        .writable(true)
        .executable(true)
        .errorMessage(null)
        .build();

    when(filePathValidator.validateFilePath("valid_file.txt")).thenReturn(validResult);
    when(filePathValidator.validateFilePath("directory_path")).thenReturn(directoryResult);

    babyNameMatcherMain = createBabyNameMatcherMain(paths);

    // Act & Assert
    babyNameMatcherMain.run();

    // Verify only first two paths were validated
    verify(filePathValidator).validateFilePath("valid_file.txt");
    verify(filePathValidator).validateFilePath("directory_path");
    verify(filePathValidator, never()).validateFilePath("should_not_reach.txt");
    verify(exceptionHandler).handle(any(InvalidFileException.class));
    verify(exceptionHandler).handle(argThat(e -> e.getMessage()
        .contains("File is a directory")));
  }
}
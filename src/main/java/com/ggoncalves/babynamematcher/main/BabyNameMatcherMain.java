package com.ggoncalves.babynamematcher.main;

import com.ggoncalves.babynamematcher.core.ConsoleNamePrinter;
import com.ggoncalves.babynamematcher.core.NameMatchProcessor;
import com.ggoncalves.babynamematcher.core.NameOption;
import com.ggoncalves.babynamematcher.di.AppComponent;
import com.ggoncalves.babynamematcher.di.AppModule;
import com.ggoncalves.babynamematcher.di.DaggerAppComponent;
import com.ggoncalves.babynamematcher.exception.NotEnoughNameListsException;
import com.ggoncalves.ggutils.console.exception.ExceptionHandler;
import com.ggoncalves.ggutils.console.exception.FilePermissionException;
import com.ggoncalves.ggutils.console.exception.InvalidFileException;
import com.ggoncalves.ggutils.console.validation.FilePathValidator;
import com.ggoncalves.ggutils.console.validation.ValidationResult;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;
import java.util.List;

@Log4j2
public class BabyNameMatcherMain {

  @Getter
  private final String[] paths;
  private final ExceptionHandler exceptionHandler;
  private final FilePathValidator filePathValidator;
  private final NameMatchProcessor nameMatchProcessor;
  private final ConsoleNamePrinter consoleNamePrinter;

  @Inject
  public BabyNameMatcherMain(String[] paths,
                             ExceptionHandler exceptionHandler,
                             FilePathValidator filePathValidator,
                             NameMatchProcessor nameMatchProcessor,
                             ConsoleNamePrinter consoleNamePrinter) {
    this.paths = paths;
    this.exceptionHandler = exceptionHandler;
    this.filePathValidator = filePathValidator;
    this.nameMatchProcessor = nameMatchProcessor;
    this.consoleNamePrinter = consoleNamePrinter;
  }

  public void run() {
    try {
      validatePaths(paths);
      List<NameOption> matchingNames = nameMatchProcessor.processAndGetMatchingNames(paths);
      consoleNamePrinter.print(matchingNames);
    }
    catch (Throwable e) {
      exceptionHandler.handle(e);
    }
  }

  private void validatePaths(String... paths) {
    if (paths.length < 2) {
      throw new NotEnoughNameListsException();
    }
    for (String path : paths) {
      ValidationResult result = filePathValidator.validateFilePath(path);
      validateResult(result);
    }
  }

  private void validateResult(ValidationResult result) {
    if (!result.isValid()) {
      throw new InvalidFileException(result.getErrorMessage());
    }
    if (!result.isExists()) {
      throw new InvalidFileException("File does not exist");
    }
    if (result.isDirectory()) {
      throw new InvalidFileException("File is a directory");
    }
    if (result.isBlank()) {
      throw new InvalidFileException("File has no content or is blank");
    }
    if (!result.isReadable()) {
      throw new FilePermissionException("Cannot read file");
    }
  }

  public static void main(String[] args) {
    AppComponent appComponent = DaggerAppComponent.builder()
        .appModule(new AppModule(args))
        .build();

    BabyNameMatcherMain main = appComponent.getMainApp();
    main.run();
  }

}

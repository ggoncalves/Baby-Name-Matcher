package com.ggoncalves.babynamematcher.main;

import com.ggoncalves.babynamematcher.BabyNameMatcherApp;
import com.ggoncalves.babynamematcher.di.AppComponent;
import com.ggoncalves.babynamematcher.di.AppModule;
import com.ggoncalves.babynamematcher.di.DaggerAppComponent;
import com.ggoncalves.babynamematcher.exception.ExceptionHandler;
import com.ggoncalves.babynamematcher.exception.FilePermissionException;
import com.ggoncalves.babynamematcher.exception.InvalidFileException;
import com.ggoncalves.babynamematcher.validator.FilePathValidator;
import com.ggoncalves.babynamematcher.validator.ValidationResult;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import javax.inject.Inject;

@Log4j2
public class BabyNameMatcherMain {

  @Getter
  private final String[] paths;
  private final ExceptionHandler exceptionHandler;
  private final FilePathValidator filePathValidator;

  @Inject
  public BabyNameMatcherMain(String[] paths,
                             ExceptionHandler exceptionHandler,
                             FilePathValidator filePathValidator) {
    this.paths = paths;
    this.exceptionHandler = exceptionHandler;
    this.filePathValidator = filePathValidator;
  }

  public void run() {
    try {
      validatePaths(paths);
      runApplication(paths);
    }
    catch (Throwable e) {
      exceptionHandler.handle(e);
    }
  }

  private void runApplication(String[] paths) {
    new BabyNameMatcherApp(paths).run();
  }

  private void validatePaths(String... paths) {
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

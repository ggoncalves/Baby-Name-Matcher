package com.ggoncalves.babynamematcher.di;

import com.ggoncalves.babynamematcher.core.ConsoleNamePrinter;
import com.ggoncalves.babynamematcher.core.NameMatchProcessor;
import com.ggoncalves.babynamematcher.core.NameListFileReader;
import com.ggoncalves.ggutils.console.exception.ExceptionHandler;
import com.ggoncalves.ggutils.console.validation.FilePathValidator;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class AppModule {

  private final String[] paths;

  public AppModule(String[] paths) {
    this.paths = paths;
  }

  @Provides
  public String[] providePaths() {
    return paths;
  }

  @Provides
  @Singleton
  public ExceptionHandler provideExceptionHandler() {
    return new ExceptionHandler();
  }

  @Provides
  @Singleton
  public FilePathValidator provideFilePathValidator() {
    return new FilePathValidator();
  }

  @Provides
  @Singleton
  public NameListFileReader provideNameListFileReader() {
    return NameListFileReader.builder().build();
  }
  @Provides
  @Singleton
  public NameMatchProcessor provideBabyNameMatcherApp(NameListFileReader nameListFileReader) {
    return new NameMatchProcessor(nameListFileReader);
  }

  @Provides
  @Singleton
  public ConsoleNamePrinter provideConsoleNamePrinter() {
    return ConsoleNamePrinter.builder().build();
  }
}
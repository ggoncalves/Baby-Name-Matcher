package com.ggoncalves.babynamematcher.di;

import com.ggoncalves.babynamematcher.BabyNameMatcherApp;
import com.ggoncalves.babynamematcher.exception.ExceptionHandler;
import com.ggoncalves.babynamematcher.validator.FilePathValidator;
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
  public BabyNameMatcherApp provideBabyNameMatcherApp() {
    return new BabyNameMatcherApp();
  }
}
package com.ggoncalves.babynamematcher.di;

import javax.inject.Singleton;

import com.ggoncalves.babynamematcher.main.BabyNameMatcherMain;

import dagger.Component;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {
  BabyNameMatcherMain getMainApp();

  @Component.Builder
  interface Builder {
    Builder appModule(AppModule appModule);

    AppComponent build();
  }
}
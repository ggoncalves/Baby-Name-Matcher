package com.ggoncalves.babynamematcher.di;

import com.ggoncalves.babynamematcher.main.BabyNameMatcherMain;
import dagger.Component;

import javax.inject.Singleton;

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
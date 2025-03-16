package com.ggoncalves.babynamematcher;

import lombok.RequiredArgsConstructor;

import java.io.File;

@RequiredArgsConstructor
public class BabyNameMatcherApp {

  private final String[] args;

  public void run() {
    printFilenames();
  }

  private void printFilenames() {
    for (String arg : args) {
      File file = new File(arg);
      System.out.println(file.getAbsolutePath());
    }
  }
}

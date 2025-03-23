package com.ggoncalves.babynamematcher.core;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;

import java.util.List;

@Builder
public class ConsoleNamePrinter {

  public void print(List<NameOption> matchingNames) {
    matchingNames.stream()
        .map(nameOption -> nameOption.getName().getNormalized())
        .forEach(this::println);
  }

  @VisibleForTesting
  void println(String string) {
    System.out.println(string);
  }
}

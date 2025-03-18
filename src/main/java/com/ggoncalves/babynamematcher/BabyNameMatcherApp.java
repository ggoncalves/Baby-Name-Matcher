package com.ggoncalves.babynamematcher;

import com.google.common.annotations.VisibleForTesting;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class BabyNameMatcherApp {

  public void run(String[] args) {
    List<List<String>> filesContent = getFilesContent(args);

    // TODO implement me
  }

  @VisibleForTesting
  @SneakyThrows
  List<List<String>> getFilesContent(String[] args) {
    List<List<String>> filesContent = new ArrayList<>(args.length);
    for (String filePath : args) {
      Path path = Paths.get(filePath);
      filesContent.add(Files.readAllLines(path));
    }
    return filesContent;
  }
}
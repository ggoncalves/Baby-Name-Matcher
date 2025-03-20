package com.ggoncalves.babynamematcher.core;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Builder
public class NameListFileReader {

  @VisibleForTesting
  @SneakyThrows
  List<List<String>> readNameListFromFiles(String[] args) {
    List<List<String>> filesContent = new ArrayList<>(args.length);
    for (String filePath : args) {
      Path path = Paths.get(filePath);
      filesContent.add(Files.readAllLines(path));
    }
    return filesContent;
  }
}

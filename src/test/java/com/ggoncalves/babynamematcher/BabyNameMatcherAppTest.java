package com.ggoncalves.babynamematcher;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class BabyNameMatcherAppTest {

  @TempDir
  Path tempDir;

  private BabyNameMatcherApp babyNameMatcherApp;

  @Test
  @DisplayName("Should process two valid list receiving two files with names")
  void testGetFilesContent() {
    // Given
    ResultFileContent fileContent1 = getResultFileContent("names1.txt","Ana", "João", "Maria");
    ResultFileContent fileContent2 = getResultFileContent("names2.txt","Pedro", "Paulo", "Carla");
    String[] args = {fileContent1.getFile().toString(), fileContent2.getFile().toString()};

    // When
    babyNameMatcherApp = new BabyNameMatcherApp();
    List<List<String>> result = babyNameMatcherApp.getFilesContent(args);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(2);
    assertThat(result.get(0)).isEqualTo(fileContent1.getContent());
    assertThat(result.get(1)).isEqualTo(fileContent2.getContent());
  }

  @SneakyThrows
  private ResultFileContent getResultFileContent(String filename, String... names) {
    Path file = tempDir.resolve(filename);
    List<String> content = Arrays.asList(names);
    Files.write(file, content);
    return new ResultFileContent(file, content);
  }

  @Data
  @AllArgsConstructor
  private static class ResultFileContent {
    private Path file;
    private List<String> content;
  }
}
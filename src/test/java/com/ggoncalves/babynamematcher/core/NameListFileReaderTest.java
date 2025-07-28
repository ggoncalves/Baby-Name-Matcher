package com.ggoncalves.babynamematcher.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class NameListFileReaderTest {

  @TempDir
  Path tempDir;

  @InjectMocks
  private NameListFileReader nameListFileReader;

  @Test
  @DisplayName("Should process two valid list receiving two files with names")
  void shouldProcessTwoValidListReceivingTwoFilesWithNames() {
    // Given
    ResultFileContent fileContent1 = getResultFileContent("names1.txt", "Ana", "João", "Maria");
    ResultFileContent fileContent2 = getResultFileContent("names2.txt", "Pedro", "Paulo", "Carla");
    String[] args = {fileContent1.getFile().toString(), fileContent2.getFile().toString()};

    // When
    List<List<String>> result = nameListFileReader.readNameListFromFiles(args);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(2);
    assertThat(result.get(0)).isEqualTo(fileContent1.getContent());
    assertThat(result.get(1)).isEqualTo(fileContent2.getContent());
  }

  @Test
  @DisplayName("Should process three valid list receiving three files with names")
  void shouldProcessThreeValidListReceivingThreeFilesWithNames() {
    // Given
    ResultFileContent fileContent1 = getResultFileContent("names1.txt", "Ana", "João", "Maria Luiza");
    ResultFileContent fileContent2 = getResultFileContent("names2.txt", "Pedro", "Paulo Roberto", "Carla");
    ResultFileContent fileContent3 = getResultFileContent("names3.txt", "Matheus Gonçalves", "Paulo", "Gláucia");
    String[] args = {fileContent1.getFile().toString(), fileContent2.getFile().toString(), fileContent3.getFile().toString()
    };

    // When
    List<List<String>> result = nameListFileReader.readNameListFromFiles(args);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.size()).isEqualTo(3);
    assertThat(result.get(0)).isEqualTo(fileContent1.getContent());
    assertThat(result.get(1)).isEqualTo(fileContent2.getContent());
    assertThat(result.get(2)).isEqualTo(fileContent3.getContent());
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
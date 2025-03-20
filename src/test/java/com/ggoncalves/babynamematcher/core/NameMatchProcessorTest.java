package com.ggoncalves.babynamematcher.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class NameMatchProcessorTest {

  @Mock
  private NameListFileReader mockNameListFileReader;

  private NameMatchProcessor nameMatchProcessor;

  @Test
  @DisplayName( "Should process two list with three matches")
  void shouldProcessTwoListWithThreeMatches() {
    // Given
    List<String> names1 = Arrays.asList("Ana", "Maria", "Julia", "Fatima");
    List<String> names2 = Arrays.asList("Ana Carolina", "Ana Julia", "Fatima", "Mariana");
    List<List<String>> nameLists = Arrays.asList(names1, names2);
    doReturn(nameLists).when(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    // When
    nameMatchProcessor = createNameMatchProcessor(true);
    List<NameOption> matchingNames =
        nameMatchProcessor.processAndGetMatchingNames(new String[]{"names1.txt", "names2" +
        ".txt"});

    // Then
    verify(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    matchingNames.forEach(System.out::println);
  }

  private NameMatchProcessor createNameMatchProcessor() {
    return createNameMatchProcessor(false);
  }

  private NameMatchProcessor createNameMatchProcessor(boolean crossCompoundNameMatch) {
    NameMatchProcessor nameMatchProcessor = new NameMatchProcessor(mockNameListFileReader);
    nameMatchProcessor.setCrossCompoundNameMatch(crossCompoundNameMatch);
    return nameMatchProcessor;
  }


}
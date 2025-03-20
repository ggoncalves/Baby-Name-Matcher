package com.ggoncalves.babynamematcher.core;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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
    nameMatchProcessor = createNameMatchProcessor();

    List<NameOption> matchingNames =
        nameMatchProcessor.processAndGetMatchingNames(new String[]{"names1.txt", "names2" +
        ".txt"});

    // Then
    verify(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    assertThat(matchingNames).asList().hasSize(7);
    assertThat(matchingNames).asList().containsExactlyElementsOf(getOrderedNameOptions());
  }

  @Test
  @DisplayName( "Should process two list with three matches using bidirectionalCompoundMatching")
  void shouldProcessTwoListWithThreeMatchesUsingBidirectionalCompoundMatching() {
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

    assertThat(matchingNames).asList().hasSize(7);
    assertThat(matchingNames).asList().containsExactlyElementsOf(getOrderedNameOptionsWithBidirectionalMatching());
  }

  @NotNull
  private NameMatchProcessor createNameMatchProcessor() {
    return createNameMatchProcessor(false);
  }

  @NotNull
  private NameMatchProcessor createNameMatchProcessor(boolean bidirectionalCompoundMatching) {
    NameMatchProcessor nameMatchProcessor = new NameMatchProcessor(mockNameListFileReader);
    nameMatchProcessor.setBidirectionalCompoundMatching(bidirectionalCompoundMatching);
    return nameMatchProcessor;
  }

  public List<NameOption> getOrderedNameOptions() {
    return Arrays.asList(
        buildNameOption("Fatima", true, 0, 1),
        buildNameOption("Ana Carolina", true, 1),
        buildNameOption("Ana Julia", true, 1),
        buildNameOption("Ana", false, 0),
        buildNameOption("Julia", false, 0),
        buildNameOption("Maria", false, 0),
        buildNameOption("Mariana", false, 1)
    );
  }

  public List<NameOption> getOrderedNameOptionsWithBidirectionalMatching() {
    return Arrays.asList(
        buildNameOption("Fatima", true, 0, 1),
        buildNameOption("Ana", true, 0),
        buildNameOption("Ana Carolina", true, 1),
        buildNameOption("Ana Julia", true, 1),
        buildNameOption("Julia", false, 0),
        buildNameOption("Maria", false, 0),
        buildNameOption("Mariana", false, 1)
    );
  }

  private NameOption buildNameOption(String name, boolean hasMatch, Integer... indices) {
    return NameOption.builder()
        .name(name)
        .hasMatch(hasMatch)
        .sourceListIndices(new HashSet<>(Arrays.asList(indices)))
        .build();
  }


}
package com.ggoncalves.babynamematcher.core;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.ggoncalves.babynamematcher.test.fixtures.NameOptionTestDataFactory.createFromAccentuatedTwoListNameOptions;
import static com.ggoncalves.babynamematcher.test.fixtures.NameOptionTestDataFactory.createFromOrderedNameOptionsWithBidirectionalMatching;
import static com.ggoncalves.babynamematcher.test.fixtures.NameOptionTestDataFactory.createFromStandardThreeListNameOptions;
import static com.ggoncalves.babynamematcher.test.fixtures.NameOptionTestDataFactory.createFromStandardTwoListNameOptions;
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
  @DisplayName("Should process two list with three matches")
  void shouldProcessTwoListWithThreeMatches() {
    // Given
    List<String> names1 = Arrays.asList("Ana", "Maria", "Julia", "Fatima");
    List<String> names2 = Arrays.asList("Ana Carolina", "Ana Julia", "Fatima", "Mariana");
    List<List<String>> nameLists = Arrays.asList(names1, names2);
    doReturn(nameLists).when(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    // When
    nameMatchProcessor = createNameMatchProcessor();

    List<NameOption> matchingNames = nameMatchProcessor.processAndGetMatchingNames(new String[]{"names1.txt", "names2" + ".txt"});

    // Then
    verify(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    assertThat(matchingNames).asList().hasSize(7);
    assertThat(matchingNames).asList().containsExactlyElementsOf(createFromStandardTwoListNameOptions());
  }

  @Test
  @DisplayName("Should process two lists ignoring accentuation")
  void shouldProcessTwoListWithIgnoringAccentuation() {
    // Given
    List<String> names1 = Arrays.asList("Aá", "Ção", "Àse", "áéíóúàèìòùçÁÉÍÓÚÀÈÌÒÙÇ");
    List<String> names2 = Arrays.asList("Aa", "Cao", "Ase", "aeiouaeioucAEIOUAEIOUC");
    List<List<String>> nameLists = Arrays.asList(names1, names2);
    doReturn(nameLists).when(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    // When
    nameMatchProcessor = createNameMatchProcessor();

    List<NameOption> matchingNames = nameMatchProcessor.processAndGetMatchingNames(new String[]{"names1.txt", "names2" + ".txt"});

    // Then
    verify(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    assertThat(matchingNames).asList().hasSize(4);
    assertThat(matchingNames).asList().containsExactlyElementsOf(createFromAccentuatedTwoListNameOptions());
  }

  @Test
  @DisplayName("Should process three lists with three matches")
  void shouldProcessThreeListsWithThreeMatches() {
    // Given
    List<String> names1 = Arrays.asList("Ana", "Maria", "Julia", "Fatima");
    List<String> names2 = Arrays.asList("Ana Carolina", "Ana Julia", "Fatima", "Mariana");
    List<String> names3 = Arrays.asList("Julieta", "Fatima", "Ana Julia", "Joaquina", "Antonia");
    List<List<String>> nameLists = Arrays.asList(names1, names2, names3);
    doReturn(nameLists).when(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    // When
    nameMatchProcessor = createNameMatchProcessor();

    List<NameOption> matchingNames = nameMatchProcessor.processAndGetMatchingNames(new String[]{"names1.txt", "names2" + ".txt"});

    // Then
    verify(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    assertThat(matchingNames).asList().hasSize(10);
    assertThat(matchingNames).asList().containsExactlyElementsOf(createFromStandardThreeListNameOptions());
  }

  @Test
  @DisplayName("Should process two list with three matches using bidirectionalCompoundMatching")
  void shouldProcessTwoListWithThreeMatchesUsingBidirectionalCompoundMatching() {
    // Given
    List<String> names1 = Arrays.asList("Ana", "Maria", "Julia", "Fatima");
    List<String> names2 = Arrays.asList("Ana Carolina", "Ana Julia", "Fatima", "Mariana");
    List<List<String>> nameLists = Arrays.asList(names1, names2);
    doReturn(nameLists).when(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    // When
    nameMatchProcessor = createNameMatchProcessor(true);

    List<NameOption> matchingNames = nameMatchProcessor.processAndGetMatchingNames(new String[]{"names1.txt", "names2" + ".txt"});

    // Then
    verify(mockNameListFileReader).readNameListFromFiles(any(String[].class));

    assertThat(matchingNames).asList().hasSize(7);
    assertThat(matchingNames).asList().containsExactlyElementsOf(createFromOrderedNameOptionsWithBidirectionalMatching());
  }

  @NotNull
  private NameMatchProcessor createNameMatchProcessor(boolean bidirectionalCompoundMatching) {
    NameMatchProcessor nameMatchProcessor = new NameMatchProcessor(mockNameListFileReader);
    nameMatchProcessor.setBidirectionalCompoundMatching(bidirectionalCompoundMatching);
    return nameMatchProcessor;
  }

  @NotNull
  private NameMatchProcessor createNameMatchProcessor() {
    return createNameMatchProcessor(false);
  }


}
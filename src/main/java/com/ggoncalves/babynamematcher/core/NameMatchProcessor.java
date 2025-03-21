package com.ggoncalves.babynamematcher.core;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class NameMatchProcessor {

  // External dependencies
  private final NameListFileReader nameListFileReader;

  private Map<NormalizedNameKey, NameOption> namesMap;
  private boolean bidirectionalCompoundMatching;

  public List<NameOption> processAndGetMatchingNames(String[] args) {
    List<List<String>> namesFromFiles = nameListFileReader.readNameListFromFiles(args);
    processNamesFromAllLists(namesFromFiles);
    return getNamesMap()
        .values()
        .stream()
        .sorted()
        .toList();
  }

  private boolean checkForMatchingNames(NormalizedNameKey[] names, Integer listIndex) {
    return Arrays.stream(names).filter(getNamesMap()::containsKey).map(getNamesMap()::get).anyMatch(nameOption -> {
      boolean isMatch = !nameOption.getSourceListIndices().contains(listIndex) || nameOption.getSourceListIndices()
          .size() > 1;

      if (isMatch && isBidirectionalCompoundMatching()) {
        nameOption.setHasMatch(true);
      }

      return isMatch;
    });
  }

  @NotNull
  @Contract(value = " -> new", pure = true)
  private Map<NormalizedNameKey, NameOption> createNamesMap() {
    return new HashMap<>();
  }

  private void createCompoundNameOptionWithMatchCheck(NormalizedNameKey name, Integer listIndex) {
    String[] splitNames = splitCompoundName(name);

    NormalizedNameKey[] normalizedNames = Arrays.stream(splitNames)
        .map(NormalizedNameKey::new)
        .toArray(NormalizedNameKey[]::new);

    boolean hasMatch = checkForMatchingNames(normalizedNames, listIndex);

    getNamesMap().put(name, NameOption.builder()
            .name(name)
            .hasMatch(hasMatch)
            .sourceListIndices(new HashSet<>(List.of(listIndex)))
            .build()
                     );
  }

  @NotNull
  private NameOption createNewOption(NormalizedNameKey name, Integer listIndex) {
    return NameOption.builder()
        .name(name)
        .sourceListIndices(new HashSet<>(List.of(listIndex)))
        .build();
  }

  private Map<NormalizedNameKey, NameOption> getNamesMap() {
    if (namesMap == null) {
      namesMap = createNamesMap();
    }
    return namesMap;
  }

  @Contract(pure = true)
  private boolean isCompoundName(@NotNull String name) {
    return name.contains(" ");
  }

  private void processNamesFromAllLists(@NotNull List<List<String>> filesContent) {

    for (int i = 0; i < filesContent.size(); i++) {
      List<String> fileContent = filesContent.get(i);
      for (String name : fileContent) {
        if (isCompoundName(name)) {
          processCompoundName(new NormalizedNameKey(name), i);
        }
        else {
          processSimpleName(new NormalizedNameKey(name), i);
        }
      }
    }
  }

  private void processCompoundName(NormalizedNameKey name, Integer listIndex) {
    Map<NormalizedNameKey, NameOption> namesMap = getNamesMap();

    if (namesMap.containsKey(name)) {
      updateExistingOption(namesMap.get(name), listIndex);
    }
    else {
      createCompoundNameOptionWithMatchCheck(name, listIndex);
    }
  }

  private void processSimpleName(NormalizedNameKey name, Integer listIndex) {
    getNamesMap().compute(name, (key, existingOption) -> Optional.ofNullable(existingOption)
        .map(option -> updateExistingOption(option, listIndex))
        .orElseGet(() -> createNewOption(name, listIndex)));
  }


  @NotNull
  @Contract(pure = true)
  private String[] splitCompoundName(@NotNull NormalizedNameKey name) {
    return name.getNormalized().split(" ");
  }

  @NotNull
  @Contract("_, _ -> param1")
  private NameOption updateExistingOption(@NotNull NameOption option, Integer listIndex) {
    if (!option.getSourceListIndices().contains(listIndex)) {
      option.setHasMatch(true);
      option.getSourceListIndices().add(listIndex);
    }
    return option;
  }
}
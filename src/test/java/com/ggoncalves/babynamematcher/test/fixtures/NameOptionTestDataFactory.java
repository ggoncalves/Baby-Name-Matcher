package com.ggoncalves.babynamematcher.test.fixtures;

import com.ggoncalves.babynamematcher.core.NameOption;
import com.ggoncalves.babynamematcher.core.NormalizedNameKey;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class NameOptionTestDataFactory {

  public static List<NameOption> createFromStandardTwoListNameOptions() {
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

  public static List<NameOption> createFromAccentuatedTwoListNameOptions() {
    return Arrays.asList(
        buildNameOption("Aa", true, 0, 1),
        buildNameOption("aeiouaeioucAEIOUAEIOUC", true, 0, 1),
        buildNameOption("Ase", true, 0, 1),
        buildNameOption("Cao", true, 0, 1)
                        );
  }

  public static List<NameOption> createFromOrderedNameOptionsWithBidirectionalMatching() {
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

  public static List<NameOption> createFromStandardThreeListNameOptions() {
    return Arrays.asList(
        buildNameOption("Fatima", true, 0, 1, 2),
        buildNameOption("Ana Julia", true, 1, 2),
        buildNameOption("Ana Carolina", true, 1),
        buildNameOption("Ana", false, 0),
        buildNameOption("Antonia", false, 2),
        buildNameOption("Joaquina", false, 2),
        buildNameOption("Julia", false, 0),
        buildNameOption("Julieta", false, 2),
        buildNameOption("Maria", false, 0),
        buildNameOption("Mariana", false, 1)
                        );
  }

  public static NameOption buildNameOption(String name, boolean isMatch, Integer... listIndices) {
    return NameOption.builder()
        .name(new NormalizedNameKey(name))
        .hasMatch(isMatch)
        .sourceListIndices(new HashSet<>(Arrays.asList(listIndices)))
        .build();
  }
}
package com.ggoncalves.babynamematcher.core;

import com.google.common.annotations.VisibleForTesting;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Builder
public class ConsoleNamePrinter {

  private static final String HEADER = "Resultado Final";
  private static final String MATCHED_NAMES_SECTION =
      "Nomes encontrados em mais de uma lista ou em nomes compostos:";
  private static final String UNMATCHED_NAMES_SECTION = "Demais nomes não encontrados em mais de uma lista:";
  private static final String NO_MATCHED_NAMES = "Nenhum nome encontrado em mais de uma lista ou em nomes compostos. " +
      "=(";

  public void print(List<NameOption> matchingNames, boolean isVerbose) {
    String matchingNamesString = buildMatchingNamesString(matchingNames);
    println(matchingNamesString);
  }

  public void print(List<NameOption> matchingNames) {
    this.print(matchingNames, false);
  }

  @VisibleForTesting
  void println(String string) {
    System.out.println(string);
  }

  private String buildMatchingNamesString(List<NameOption> matchingNames) {
    Map<Boolean, List<NameOption>> matchedToListOfNameOptionMap = matchingNames.stream()
        .collect(Collectors.partitioningBy(NameOption::isHasMatch));

    StringBuilder builder = new StringBuilder();
    mountHeader(builder);

    if (hasAnyMatchedName(matchedToListOfNameOptionMap)) {
      mountMatchedNamesSection(builder);
      mountNameOptionList(builder, matchedToListOfNameOptionMap.get(true));
      builder.append(System.lineSeparator());
    }
    else {
      mountNoMatchedNamesSection(builder);
    }
    mountUnmatchedNamesSection(builder);
    mountNameOptionList(builder, matchedToListOfNameOptionMap.get(false));
    return builder.toString();
  }

  private boolean hasAnyMatchedName(Map<Boolean, List<NameOption>> matchedToListOfNameOptionMap) {
    return !matchedToListOfNameOptionMap.get(true).isEmpty();
  }

  private void mountSection(StringBuilder stringBuilder, String text) {
    stringBuilder.append(text)
        .append(System.lineSeparator().repeat(2));
  }

  private void mountHeader(StringBuilder stringBuilder) {
    mountSection(stringBuilder, HEADER);
  }

  private void mountMatchedNamesSection(StringBuilder stringBuilder) {
    mountSection(stringBuilder, MATCHED_NAMES_SECTION);
  }

  private void mountUnmatchedNamesSection(StringBuilder stringBuilder) {
    mountSection(stringBuilder, UNMATCHED_NAMES_SECTION);
  }

  private void mountNoMatchedNamesSection(StringBuilder stringBuilder) {
    mountSection(stringBuilder, NO_MATCHED_NAMES);
  }

  private void mountNameOptionList(StringBuilder stringBuilder, List<NameOption> nameOptions) {
    nameOptions.stream()
        .map(nameOption -> nameOption.getName().getNormalized())
        .forEach(name -> stringBuilder.append(name).append(System.lineSeparator()));
  }
}

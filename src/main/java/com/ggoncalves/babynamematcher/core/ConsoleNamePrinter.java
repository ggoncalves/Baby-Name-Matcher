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
  private static final String FOUND_IN_N_LISTS = "(encontrado em %d listas)";
  private static final String FOUND_IN_OTHER_NAME_LIST = "(encontrado parte do nome em outra lista)";

  public void print(List<NameOption> matchingNames, boolean isVerbose) {
    String matchingNamesString = buildMatchingNamesString(matchingNames, isVerbose);
    println(matchingNamesString);
  }

  public void print(List<NameOption> matchingNames) {
    this.print(matchingNames, false);
  }

  @VisibleForTesting
  void println(String string) {
    System.out.println(string);
  }

  private String buildMatchingNamesString(List<NameOption> matchingNames, boolean isVerbose) {
    Map<Boolean, List<NameOption>> matchedToListOfNameOptionMap = matchingNames.stream()
        .collect(Collectors.partitioningBy(NameOption::isHasMatch));

    StringBuilder builder = new StringBuilder();
    mountHeader(builder);

    if (hasAnyMatchedName(matchedToListOfNameOptionMap)) {
      mountMatchedNamesSection(builder);
      mountNameOptionList(builder, true, matchedToListOfNameOptionMap, isVerbose);
      builder.append(System.lineSeparator());
    }
    else {
      mountNoMatchedNamesSection(builder);
    }
    mountUnmatchedNamesSection(builder);
    mountNameOptionList(builder, false, matchedToListOfNameOptionMap, isVerbose);
    return builder.toString();
  }

  private boolean hasAnyMatchedName(Map<Boolean, List<NameOption>> matchedToListOfNameOptionMap) {
    return !matchedToListOfNameOptionMap.get(true).isEmpty();
  }

  private void mountHeader(StringBuilder stringBuilder) {
    mountSection(stringBuilder, HEADER);
  }

  private void mountMatchedNamesSection(StringBuilder stringBuilder) {
    mountSection(stringBuilder, MATCHED_NAMES_SECTION);
  }

  private void mountNameOptionList(StringBuilder stringBuilder, boolean isMatched, Map<Boolean,
      List<NameOption>> matchedToListOfNameOptionMap, boolean isVerbose) {
    matchedToListOfNameOptionMap.get(isMatched)
        .forEach(nameOption -> {
          String name = nameOption.getName().getNormalized();
          stringBuilder.append(name);
          if (isMatched && isVerbose) {
            stringBuilder.append(" ");
            int listCount = nameOption.getSourceListIndices().size();
            if (listCount > 1) {
              stringBuilder.append(String.format(FOUND_IN_N_LISTS, listCount));
            }
            else {
              stringBuilder.append(FOUND_IN_OTHER_NAME_LIST);
            }
          }
          stringBuilder.append(System.lineSeparator());
        });
  }

  private void mountNoMatchedNamesSection(StringBuilder stringBuilder) {
    mountSection(stringBuilder, NO_MATCHED_NAMES);
  }

  private void mountSection(StringBuilder stringBuilder, String text) {
    stringBuilder.append(text)
        .append(System.lineSeparator().repeat(2));
  }

  private void mountUnmatchedNamesSection(StringBuilder stringBuilder) {
    mountSection(stringBuilder, UNMATCHED_NAMES_SECTION);
  }
}

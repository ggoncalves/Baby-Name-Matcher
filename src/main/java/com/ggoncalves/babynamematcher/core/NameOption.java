package com.ggoncalves.babynamematcher.core;

import lombok.Builder;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Builder
public class NameOption implements Comparable<NameOption>{
  private NormalizedNameKey name;
  private boolean hasMatch;

  @Builder.Default
  private Set<Integer> sourceListIndices = new HashSet<>();

  @Override
  public int compareTo(@NotNull NameOption o) {
    if (!Objects.equals(sourceListIndices.size(), o.sourceListIndices.size())) {
      return o.sourceListIndices.size() - sourceListIndices.size();
    }
    if (!Objects.equals(hasMatch, o.hasMatch)) {
      return hasMatch ? -1 : 1;
    }
    return name.getNormalized().compareTo(o.getName().getNormalized());
  }

//  public static String normalizePreservingCase(String input) {
//    if (input == null) return "";
//
//    StringBuilder result = new StringBuilder(input.length());
//
//    for (int i = 0; i < input.length(); i++) {
//      String c = String.valueOf(input.charAt(i));
//      String normalized = Normalizer.normalize(c, Normalizer.Form.NFD)
//          .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
//      result.append(normalized.isEmpty() ? "" : normalized);
//    }
//
//    return result.toString();
//  }
}

package com.ggoncalves.babynamematcher.core;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class NameOption implements Comparable<NameOption>{
  private String name;
  private boolean hasMatch;
  private Set<Integer> sourceListIndices = new HashSet<>();

  @Override
  public int compareTo(@NotNull NameOption o) {
    if (!Objects.equals(sourceListIndices.size(), o.sourceListIndices.size())) {
      return o.sourceListIndices.size() - sourceListIndices.size();
    }
    if (!Objects.equals(hasMatch, o.hasMatch)) {
      return hasMatch ? -1 : 1;
    }
    return name.compareTo(o.name);
  }
}

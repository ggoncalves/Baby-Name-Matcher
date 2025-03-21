package com.ggoncalves.babynamematcher.core;

import lombok.Data;

import java.text.Normalizer;
import java.util.Objects;
import java.util.regex.Pattern;

@Data
public class NormalizedNameKey {
  private final String original;
  private final String normalized;
  private static final Pattern DIACRITICS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

  public NormalizedNameKey(String original) {
    this.original = original;
    this.normalized = normalize(original);
  }

  private static String normalize(String input) {
    if (input == null) return null;
    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    return DIACRITICS_PATTERN.matcher(normalized).replaceAll("").toLowerCase();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NormalizedNameKey that = (NormalizedNameKey) o;
    return Objects.equals(normalized, that.normalized);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(normalized);
  }

  @Override
  public String toString() {
    return original;
  }
}

package com.ggoncalves.babynamematcher.core;

import java.text.Normalizer;
import java.util.Objects;
import java.util.regex.Pattern;

import com.google.common.annotations.VisibleForTesting;

import lombok.Data;

@Data
public class NormalizedNameKey {
  private static final Pattern DIACRITICS_PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
  private final String original;
  private final String normalized;

  public NormalizedNameKey(String original) {
    this.original = original;
    this.normalized = normalize(original);
  }

  @VisibleForTesting
  String normalize(String input) {
    if (input == null) return null;

    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    String withoutDiacritics = DIACRITICS_PATTERN.matcher(normalized).replaceAll("");

    String[] words = withoutDiacritics.split("\\s+");
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < words.length; i++) {
      if (!words[i].isEmpty()) {
        String word = words[i].toLowerCase();
        result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1));

        if (i < words.length - 1) {
          result.append(" ");
        }
      }
    }

    return result.toString();
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(normalized);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    NormalizedNameKey that = (NormalizedNameKey) o;
    return Objects.equals(normalized, that.normalized);
  }

  @Override
  public String toString() {
    return original;
  }
}

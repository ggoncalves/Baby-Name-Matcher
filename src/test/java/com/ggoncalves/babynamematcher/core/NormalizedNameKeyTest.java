package com.ggoncalves.babynamematcher.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class NormalizedNameKeyTest {

  @InjectMocks
  private NormalizedNameKey normalizedNameKey;

  @Test
  @DisplayName("Should construct using right normalization")
  void shouldConstructUsingRightNormalization() {
    NormalizedNameKey nameKey = new NormalizedNameKey("Fátima");
    assertThat(nameKey.getNormalized()).isEqualTo("Fatima");

    nameKey = new NormalizedNameKey("çÇñÑ");
    assertThat(nameKey.getNormalized()).isEqualTo("Ccnn");

    nameKey = new NormalizedNameKey("José ÔMÉGÀ");
    assertThat(nameKey.getNormalized()).isEqualTo("Jose Omega");
  }

  @Test
  @DisplayName("Should normalize text")
  public void shouldNormalizeText() {
    assertThat(normalizedNameKey.normalize("Fátima")).isEqualTo("Fatima");
    assertThat(normalizedNameKey.normalize("José ÔMÉGÀ")).isEqualTo("Jose Omega");
    assertThat(normalizedNameKey.normalize("çÇñÑ")).isEqualTo("Ccnn");
    assertThat(normalizedNameKey.normalize("ANA MARÍA")).isEqualTo("Ana Maria");
    assertThat(normalizedNameKey.normalize("joão dA siLVA")).isEqualTo("Joao Da Silva");
  }

}
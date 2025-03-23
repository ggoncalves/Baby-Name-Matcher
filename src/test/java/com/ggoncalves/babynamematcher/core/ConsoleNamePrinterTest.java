package com.ggoncalves.babynamematcher.core;

import com.ggoncalves.babynamematcher.test.fixtures.NameOptionTestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConsoleNamePrinterTest {

  @InjectMocks
  @Spy
  private ConsoleNamePrinter consoleNamePrinter = new ConsoleNamePrinter();

  @Test
  @DisplayName("Should print 3 matching names")
  void shouldPrintThreeMatchingNames() {
    // Given
    List<NameOption> threeMatchingNames = NameOptionTestDataFactory.createFromStandardTwoListNameOptions();

    // When
    consoleNamePrinter.print(threeMatchingNames);

    // Then
    verify(consoleNamePrinter).println(getExpectedThreeMatchingNamesOutput());
  }

  @Test
  @DisplayName("Should print message for no matching names")
  void shouldPrintMessageForNoMatchingNames() {
    // Given
    List<NameOption> noMatchingNames = NameOptionTestDataFactory.createNoMatchingNameOptions();

    // When
    consoleNamePrinter.print(noMatchingNames);

    // Then
    verify(consoleNamePrinter).println(getExpectedNoMatchingNamesOutput());
  }

  private String getExpectedThreeMatchingNamesOutput() {
    return
        buildStringWithLineBreaks("Resultado Final", 2) +
        buildStringWithLineBreaks("Nomes encontrados em mais de uma lista ou em nomes compostos:", 2) +
        buildStringWithLineBreaks("Fatima") +
        buildStringWithLineBreaks("Ana Carolina") +
        buildStringWithLineBreaks("Ana Julia", 2) +
        buildStringWithLineBreaks("Demais nomes não encontrados em mais de uma lista:", 2) +
        buildStringWithLineBreaks("Ana") +
        buildStringWithLineBreaks("Julia") +
        buildStringWithLineBreaks("Maria") +
        buildStringWithLineBreaks("Mariana");
  }

  private String getExpectedNoMatchingNamesOutput() {
    return
        buildStringWithLineBreaks("Resultado Final", 2) +
        buildStringWithLineBreaks("Nenhum nome encontrado em mais de uma lista ou em nomes compostos. =(", 2) +
        buildStringWithLineBreaks("Demais nomes não encontrados em mais de uma lista:", 2) +
        buildStringWithLineBreaks("Ana") +
        buildStringWithLineBreaks("Julia") +
        buildStringWithLineBreaks("Maria") +
        buildStringWithLineBreaks("Mariana");
  }

  private String buildStringWithLineBreaks(String s, int numberOfLineBreaks) {
    return s + System.lineSeparator().repeat(numberOfLineBreaks);
  }

  private String buildStringWithLineBreaks(String s) {
    return buildStringWithLineBreaks(s, 1);
  }

}
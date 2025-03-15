package com.ggoncalves.babynamematcher.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ExceptionHandler {

  public static final String INVALID_FILE_ERROR = "ERRO: O arquivo fornecido é inválido - {}";
  public static final String PERMISSION_ERROR_MESSAGE = "Erro de permissão: {}";
  public static final String UNEXPECTED_ERROR_MESSAGE = "ERRO: Ocorreu um erro inesperado - ";

  public void handle(Exception e) {
    if (e instanceof InvalidFileException) {
      log.error(INVALID_FILE_ERROR, e.getMessage());
//      System.err.println(INVALID_FILE_ERROR + e.getMessage());
    }
    else if (e instanceof FilePermissionException) {
      log.error(PERMISSION_ERROR_MESSAGE, e.getMessage());
//      System.err.println(FILE_ACCESS_DENIED_MESSAGE + e.getMessage());
    }
    else {
      log.error(UNEXPECTED_ERROR_MESSAGE + e.getMessage(), e);
//      System.err.println(UNEXPECTED_ERROR_MESSAGE + e.getMessage());
    }

    if (!(e instanceof InvalidFileException) && !(e instanceof FilePermissionException)) {
      log.debug("Stack trace:", e);
    }
  }
}
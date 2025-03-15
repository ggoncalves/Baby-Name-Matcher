package com.ggoncalves.babynamematcher.exception;

import lombok.Getter;

@Getter
public class FilePermissionException extends RuntimeException {

  public FilePermissionException(String message) {
    super(message);
  }

  public FilePermissionException(String message, Throwable cause) {
    super(message, cause);
  }
}
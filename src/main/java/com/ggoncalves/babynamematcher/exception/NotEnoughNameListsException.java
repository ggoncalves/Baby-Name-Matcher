package com.ggoncalves.babynamematcher.exception;

public class NotEnoughNameListsException extends IllegalArgumentException {

  public NotEnoughNameListsException() {
    super("At least two name lists are required for matching");
  }
}
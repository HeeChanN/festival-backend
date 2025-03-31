package com.halo.eventer.global.error.exception;

import com.halo.eventer.global.error.ErrorCode;

public class InvalidInputException extends BaseException {
  public InvalidInputException(final String message) {
    super(message, ErrorCode.INVALID_INPUT_VALUE);
  }
}

package com.halo.eventer.global.error.exception;

import com.halo.eventer.global.error.ErrorCode;

public class ConflictException extends BaseException {
  public ConflictException(String message,ErrorCode errorCode) {
    super(message, errorCode);
  }
  public ConflictException(ErrorCode errorCode) {
    super(errorCode);
  }
}

package com.halo.eventer.global.utils;

import com.halo.eventer.global.error.ErrorCode;
import com.halo.eventer.global.error.exception.BaseException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class EncryptService {
  private final AesBytesEncryptor aesBytesEncryptor;

  public String encryptInfo(String info) {
    try {
      byte[] encryptedBytes = aesBytesEncryptor.encrypt(info.getBytes(StandardCharsets.UTF_8));
      return Base64.getEncoder().encodeToString(encryptedBytes);
    } catch (BaseException e) {
      throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }

  public String decryptInfo(String encryptedInfo) {
    try {
      byte[] encryptedBytes = Base64.getDecoder().decode(encryptedInfo);
      byte[] decryptedBytes = aesBytesEncryptor.decrypt(encryptedBytes);
      return new String(decryptedBytes, StandardCharsets.UTF_8);
    } catch (BaseException e) {
      throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR);
    }
  }
}

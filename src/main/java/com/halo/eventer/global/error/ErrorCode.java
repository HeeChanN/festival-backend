package com.halo.eventer.global.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ErrorCode {

    // Common Error
    INTERNAL_SERVER_ERROR("C001", "Server Error", 500),
    INVALID_INPUT_VALUE("C002", "Invalid Input Value", 400),
    METHOD_NOT_ALLOWED("C003", "Invalid HTTP Method", 405),
    ENTITY_NOT_FOUND("C004", "Entity Not Found", 400),
    CONFLICT("C005", "Conflict Occurred", 400),
    UNACCEPTABLE_EXTENSION("C007", "Unacceptable Extension", 400),
    INVALID_JSON_FORMAT("C008", "Invalid JSON Format", 400),
    MISSING_PARAMETER("C009", "Missing Parameter", 400),
    INVALID_PARAMETER_TYPE("C010", "Invalid Parameter Type", 400),
    MISSING_PATH_VARIABLE("C011", "Missing Path Variable", 400),
    FORBIDDEN("C012", "Forbidden", 400),
    ERR_DATA_INTEGRITY_VIOLATION("E001", "Data integrity violation", 409),
    VALIDATION_FAILED("C013", "Validation Failed", 400),

    // Festival
    SUB_ADDRESS_ALREADY_EXISTS("F001", "subAddress Already Exists", 400),

    // DownWidget Error
    PERMIT_THREE_ELEMENT("DW001", "Only Permit Three Element", 400),

    // Vote Error
    ALREADY_LIKE("C006", "Already Like Element", 400),

    // Auth
    LOGIN_FAILED("A001", "Login Failed", 401),
    UN_AUTHENTICATED("A002", "Unauthenticated", 401),
    UN_AUTHORIZED("A003", "Unauthorized", 403),

    // StampUser
    STAMP_USER_ALREADY_EXISTS("SU001", "StampUser Already Exists", 400),

    // Infra
    SMS_SEND_FAILED("I001", "SMS Send Failed", 500),
    ;

    private final String code;
    private final String message;
    private final int status;

    ErrorCode(final String code, final String message, final int status) {
        this.message = message;
        this.code = code;
        this.status = status;
    }
}

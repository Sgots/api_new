/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.smartcom.api.exception.BadRequestException
 *  org.springframework.http.HttpStatus
 *  org.springframework.web.bind.annotation.ResponseStatus
 */
package com.smartcom.api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BadRequestException
        extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}


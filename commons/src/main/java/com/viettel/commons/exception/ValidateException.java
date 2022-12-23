package com.viettel.commons.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class ValidateException extends RuntimeException {
    private final List<FieldViolation> fields;

    public ValidateException(List<FieldViolation> fields) {
        this(null, fields);
    }

    public ValidateException(String msg, List<FieldViolation> fields) {
        super(msg);
        this.fields = fields;
    }

}

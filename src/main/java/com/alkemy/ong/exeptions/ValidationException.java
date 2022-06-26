package com.alkemy.ong.exeptions;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
@Schema(description = "This object wraps and contains a description of the error that occurred,"
        + " in addition to wrapping validation errors.")
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = -4616176975082518741L;
    private final List<FieldError> fieldErrors;

    public ValidationException(List<FieldError> fieldErrors) {
        super("There are validation errors.");
        this.fieldErrors = fieldErrors;
    }

    public List<FieldError> getErrors() {
        return this.fieldErrors;
    }

}

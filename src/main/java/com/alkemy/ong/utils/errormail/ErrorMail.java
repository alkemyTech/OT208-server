package com.alkemy.ong.utils.errormail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ErrorMail {

    private List<Error> errors;

    @Getter
    @Setter
    @ToString
    public static class Error {

        private String message;

        private String field;

        private String help;
    }
}

package com.github.leandrolimasi.exception;

/**
 * Created by leandrolimadasilva on 26/11/2017.
 */
public class LogParserException extends RuntimeException {

    public LogParserException(String message) {
        super(message);
    }

    public LogParserException(String message, Object... params) {
        super(String.format(message, params));
    }

}

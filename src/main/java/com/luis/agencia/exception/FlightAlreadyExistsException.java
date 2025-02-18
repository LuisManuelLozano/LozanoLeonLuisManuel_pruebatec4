// FlightAlreadyExistsException.java
package com.luis.agencia.exception;

public class FlightAlreadyExistsException extends RuntimeException {
    public FlightAlreadyExistsException(String message) {
        super(message);
    }
}

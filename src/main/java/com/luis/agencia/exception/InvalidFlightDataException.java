// InvalidFlightDataException.java
package com.luis.agencia.exception;

public class InvalidFlightDataException extends IllegalArgumentException { // Extiende de IllegalArgumentException
    public InvalidFlightDataException(String message) {
        super(message);
    }
}

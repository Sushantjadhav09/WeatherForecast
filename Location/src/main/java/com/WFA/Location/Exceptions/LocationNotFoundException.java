package com.WFA.Location.Exceptions;

public class LocationNotFoundException
        extends RuntimeException {

    public LocationNotFoundException(String message) {
        super(message);
    }
}

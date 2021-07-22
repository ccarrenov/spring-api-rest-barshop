package com.barshop.app.exception;


public class NumberPageException extends Exception {

    private static final long serialVersionUID = -2421908667625243481L;

    public NumberPageException(String message) {
        super(message);
    }
    
    public NumberPageException(String message, Throwable ex) {
        super(message, ex);
    }
}

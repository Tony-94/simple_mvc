package org.example.app.exceptions;

public class UploadFileExceptions extends Exception{
    private final String message;


    public UploadFileExceptions(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

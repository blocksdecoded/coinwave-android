package com.makeuseof.muocore.exceptions;

/**
 * Created by Alisher Alikulov on 12/26/16.
 * for project MuoLibTest
 * with Android Studio
 */

public class MuoCoreContextNotInitializedException extends RuntimeException {
    public MuoCoreContextNotInitializedException(){
        super("MuoCoreContext is not initialized! Please call MuoCoreContext.init() method before using MUO Core library features.");
    }
}

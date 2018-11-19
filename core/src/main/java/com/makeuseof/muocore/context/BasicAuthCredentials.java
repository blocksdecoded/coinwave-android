package com.makeuseof.muocore.context;

/**
 * Created by Alisher Alikulov on 1/17/17.
 * for project makeuseof-android
 * with Android Studio
 */

public class BasicAuthCredentials {
    private String username;
    private String password;

    private BasicAuthCredentials(){}

    public static BasicAuthCredentials make(String username, String password) {
        if(username != null && password!=null){
            BasicAuthCredentials credentials = new BasicAuthCredentials();
            credentials.password = password;
            credentials.username = username;
            return credentials;
        }
        return null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}

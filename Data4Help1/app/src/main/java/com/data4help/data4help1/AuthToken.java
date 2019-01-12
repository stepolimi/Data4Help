package com.data4help.data4help1;

public class AuthToken {
    private static String id;

    public AuthToken(String id){
        AuthToken.id = id;
    }

    public static String getId() {
        return id;
    }

    public void setId(String id) {
        AuthToken.id = id;
    }
}


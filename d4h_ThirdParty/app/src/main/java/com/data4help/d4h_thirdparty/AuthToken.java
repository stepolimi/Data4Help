package com.data4help.d4h_thirdparty;

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

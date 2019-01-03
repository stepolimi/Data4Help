package com.data4help.d4h_thirdparty;

public class Config {
    //url
    public static final String LOGINURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/thirdParties/login";
    public static final String REGISTRATIONURL ="http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/thirdParties/registration";
    public static final String PERSONALDATAURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/thirdParties/insertPersonalData";
    public static final String GROUPREQUESTURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/thirdParties/";
    public static final String SINGLEREQUESTURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/thirdParties/";
    public static final String SUBSCRIBEURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/thirdParties/";

    //error strings
    public static final String EMPTYFIELDS = "Some fields are empty. You must fill all of them!";
    public static final String INCORRECTPIVA = "The Piva is incorrect!";
    public static final String SHORTPASSWORD = "The password must contain at least 8 elements.";
    public static final String SERVERERROR = "Server problem. Try again later.";
    public static final String WRONGEMAIL = "THe entered email is wrong, please insert a correct one";
    public static final String PRESENCENUMBERORSYMBOLS = "Some fields must not contain numbers or symbols.";
    public static final String INSERTNUMBER = "A number must be insert in the fiscal code area.";
    public static final String POSITIVEGROUPREQUEST = "The app has found more than 1000 users which respect all request constraints.\n" +
            "The request has been accepted.\n" + "If you want you can subscribe for receiving more data.\n";
    public static final String POSITIVESINGLEREQUEST = "The single user has accepted your request! If you want you can subscribe for receiving more data.";

}

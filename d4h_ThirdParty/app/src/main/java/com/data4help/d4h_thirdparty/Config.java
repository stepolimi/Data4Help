package com.data4help.d4h_thirdparty;

import android.content.res.Resources;

public class Config {
    //url
    private static final String domain =  Resources.getSystem().getString(R.string.domain);
    private static final String  URL = "http://" + domain + ":8080/d4h-server-0.0.1-SNAPSHOT/";
    public static final String LOGINURL = URL + "api/thirdParties/login";
    public static final String REGISTRATIONURL =URL + "api/thirdParties/registration";
    public static final String PERSONALDATAURL = URL + "api/thirdParties/insertPersonalData";
    public static final String GROUPREQUESTURL = URL + "api/thirdParties/createGroupRequest";
    public static final String SINGLEREQUESTURL = URL + "api/thirdParties/createUserRequest";
    public static final String GETGROUPDATAURL = URL + "api/thirdParties/getGroupData";
    public static final String GETUSERDATAURL = URL + "api/thirdParties/getUserData";
    public static final String GETAQUIREDGROUPDATAURL = URL + "api/thirdParties/getAcquiredGroupData";
    public static final String GETAQUIREDUSERURL = URL + "api/thirdParties/getAcquiredUserData";
    public static final String SUBSCRIBEGROUPURL = URL + "api/thirdParties/subscribeGroup";
    public static final String SUBSCRIBEUSERURL = URL + "api/thirdParties/subscribeUser";
    public static final String GETPENDINGREQUESTURL = URL + "api/thirdParties/getPendingRequests";

    //error strings
    public static final String EMPTYFIELDS = "Some fields are empty. You must fill all of them!";
    public static final String INCORRECTPIVA = "The Piva is incorrect!";
    public static final String SHORTPASSWORD = "The password must contain at least 8 elements.";
    public static final String SERVERERROR = "Server problem. Try again later.";
    public static final String WRONGEMAIL = "THe entered email is wrong, please insert a correct one";
    public static final String PRESENCENUMBERORSYMBOLS = "Some fields must not contain numbers or symbols.";
    public static final String INCORRECTFISCALCODE = "The fiscal code is incorrect!";
    public static final String INSERTNUMBER = "A number must be insert in the fiscal code area.";
    public static final String POSITIVEGROUPREQUEST = "The app has found more than 1000 users which respect all request constraints.\n" +
            "The request has been accepted.\n" + "If you want you can subscribe for receiving more data.\n";
    public static final String POSITIVESINGLEREQUEST = "The single user has accepted your request! If you want you can subscribe for receiving more data.";

    public static final String BADREQUEST = "A bad request error has occurred! Try again.";
    public static final String UNAUTHORIZED = "An unauthorized error has occurred! Try again";
    public static final String NOTFOUND = "404 url not found! \n" +
            "Did you correctly change the server IP? \n" +
            "It is settled in the XML string.xml which can be found in path ./res/values/string.xml" ;
    public static final String INTERNALSERVERERROR = "An internal server error occurred!";

    public static final String NOTIMPLEMENTED = "Work in progress, this field isn't ready yet.";
    public static final String NOSINGLEUSERREQUESTS= "There are no single user requests waiting answer.";
    public static final String PLEASEWAIT = "Please wait...";

    public static final int DAYTWO= 2;
    public static final int DAYTHREE = 3;
    public static final int DAYFOUR = 4;
    public static final int DAYFIVE = 5;
    public static final int DAYSIX = 6;
    public static final int DAYSEVEN = 7;
}

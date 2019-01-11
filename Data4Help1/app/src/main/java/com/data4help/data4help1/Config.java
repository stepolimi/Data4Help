package com.data4help.data4help1;

import android.content.res.Resources;

public class Config {
    //url
    private static final String domain = Resources.getSystem().getString(R.string.domain);
    private static final String URL = "http://"+ domain + "/d4h-server-0.0.1-SNAPSHOT/";
    public static final String LOGINURL = URL +"api/users/login";
    public static final String REGISTRATIONURL = URL +"api/users/registration";
    public static final String PERSONALDATAURL = URL + "api/users/insertPersonalData";
    public static final String SETTINGSURL = URL + "api/users/insertWeightHeight";
    public static final String DAILYHEALTHPARAMURL = URL + "api/users/getDailyHealthParam";
    public static final String WEEKLYHEALTHPARAMURL = URL + "api/users/getWeeklyHealthParam";
    public static final String MONTHLYHEALTHPARAMURL = URL + "api/users/getMonthlyHealthParam";
    public static final String YEARHEALTHPARAMURL = URL + "api/users/getYearHealthParam";
    public static final String THIRDPARTYNOTIFICATIONURL = URL + "api/users/getRequests";
    public static final String SENDPARAMURL = URL + "api/users/addHealthParam";
    public static final String ACCEPTORDENIEURL = URL + "api/users/respondRequest";


    //error strings
    public static final String EMPTYFIELDS = "Some fields are empty. You must fill all of them!";
    public static final String INCORRECTFISCALCODE = "The fiscal code is incorrect!";
    public static final String SHORTPASSWORD = "The password must contain at least 8 elements.";
    public static final String SERVERERROR ="Server problem. Try again later.";
    public static final String INCORRECTCOUNTRY = "The app is only working in Italy, insert Italy.";
    public static final String INCORRECTREGION = "You can only add italian regions in english.";
    public static final String WRONGEMAIL = "The entered email is wrong, please insert a correct one";
    public static final String PRESENCENUMBERORSYMBOLS = "Some fields must not contain numbers or symbols.";
    public static final String BADREQUEST = "A bad request error has occurred! Try again.";
    public static final String UNAUTHORIZED = "An unauthorized error has occurred! Try again";
    public static final String NOTFOUND = "404 url not found! \n" +
            "Did you correctly change the server IP? \n" +
            "It is settled in the XML string.xml which can be found in path ./res/values/string.xml" ;
    public static final String INTERNALSERVERERROR = "An internal server error occurred!";
    public static final String PLEASEWAIT = "Please wait...";

    //health param high-low values
    public static final int MINHEARTBEAT = 60;
    public static final int MAXHEARTBEAT = 100;
    public static final int MINMAXPRESSURE = 80;
    public static final int MAXMAXPRESSURE = 120;
    public static final int MINMINPRESSURE = 50;
    public static final int MAXMINPRESSURE = 80;
    public static final float MINTEMPERATURE = 36;
    public static final float MAXTEMPERATURE = 37;

    //notification
    public static final String NOREQUESTS = "There are no requests coming from third parties.";
}

package com.data4help.data4help1;

public class Config {
    //url
    public static final String LOGINURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/login";
    public static final String REGISTRATIONURL ="http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/registration";
    public static final String PERSONALDATAURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/insertPersonalData";
    public static final String SETTINGSURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/insertWeightHeight";
    public static final String DAILYHEALTHPARAM = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getDailyHealthParam";
    public static final String WEEKLYHEALTHPARAM = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getWeeklyHealthParam";
    public static final String MONTHLYHEALTHPARAM = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getMonthlyHealthParam";
    public static final String YEARHEALTHPARAM = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getYearHealthParam";
    public static final String THIRDPARTYNOTIFICATION = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getYearHealthParam";


    //error strings
    public static final String EMPTYFIELDS = "Some fields are empty. You must fill all of them!";
    public static final String INCORRECTFISCALCODE = "The fiscal code is incorrect!";
    public static final String SHORTPASSWORD = "The password must contain at least 8 elements.";
    public static final String SERVERERROR ="Server problem. Try again later.";
}

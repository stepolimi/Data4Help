package com.data4help.data4help1;

public class Config {
    //url
    public static final String LOGINURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/login";
    public static final String REGISTRATIONURL ="http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/registration";
    public static final String PERSONALDATAURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/insertPersonalData";
    public static final String SETTINGSURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/insertWeightHeight";
    public static final String DAILYHEALTHPARAMURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getDailyHealthParam";
    public static final String WEEKLYHEALTHPARAMURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getWeeklyHealthParam";
    public static final String MONTHLYHEALTHPARAMURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getMonthlyHealthParam";
    public static final String YEARHEALTHPARAMURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/getYearHealthParam";
    public static final String THIRDPARTYNOTIFICATIONURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/";
    public static final String SENDPARAMURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/";
    public static final String ACCEPTORDENIEURL = "http://192.168.0.143:8080/d4h-server-0.0.1-SNAPSHOT/api/users/";


    //error strings
    public static final String EMPTYFIELDS = "Some fields are empty. You must fill all of them!";
    public static final String INCORRECTFISCALCODE = "The fiscal code is incorrect!";
    public static final String SHORTPASSWORD = "The password must contain at least 8 elements.";
    public static final String SERVERERROR ="Server problem. Try again later.";
    public static final String INCORRECTCOUNTRY = "The app is only working in Europe, insert an european country.";


    //health param high-low values
    public static final int MINHEARTBEAT = 60;
    public static final int MAXHEARTBEAT = 100;
    public static final int MINMAXPRESSURE = 80;
    public static final int MAXMAXPRESSURE = 120;
    public static final int MINMINPRESSURE = 50;
    public static final int MAXMINPRESSURE = 80;
    public static final double MINTEMPERATURE = 36.0;
    public static final double MAXTEMPERATURE = 37.2;

    //country
    static final String AUSTRIA = "austria";
    static final String BELGIUM = "belgium";
    static final String BULGARIA = "bulgaria";
    static final String CROATIA = "croatia";
    static final String CYPRUS = "cyprus";
    static final String CZECHIA = "czechia";
    static final String DENMARK = "denmark";
    static final String ESTONIA = "estonia";
    static final String FINLAND = "finland";
    static final String FRANCE = "france";
    static final String GERMANY = "germany";
    static final String GREECE = "greece";
    static final String HUNGARY = "hungary";
    static final String IRELAND = "ireland";
    static final String ITALY = "italy";

}

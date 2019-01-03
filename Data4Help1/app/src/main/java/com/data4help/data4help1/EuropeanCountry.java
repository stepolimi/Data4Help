package com.data4help.data4help1;

import java.util.ArrayList;
import java.util.List;

import static com.data4help.data4help1.Config.*;

public class EuropeanCountry {
    private List<String> europeanCountry = new ArrayList<>();

    public EuropeanCountry(){
        europeanCountry.add(AUSTRIA);
        europeanCountry.add(BELGIUM);
        europeanCountry.add(BULGARIA);
        europeanCountry.add(CROATIA);
        europeanCountry.add(CYPRUS);
        europeanCountry.add(CZECHIA);
        europeanCountry.add(DENMARK);
        europeanCountry.add(ESTONIA);
        europeanCountry.add(FINLAND);
        europeanCountry.add(FRANCE);
        europeanCountry.add(GERMANY);
        europeanCountry.add(GREECE);
        europeanCountry.add(HUNGARY);
        europeanCountry.add(IRELAND);
        europeanCountry.add(ITALY);
    }


    public List<String> getEuropeanCountry() {
        return europeanCountry;
    }

}

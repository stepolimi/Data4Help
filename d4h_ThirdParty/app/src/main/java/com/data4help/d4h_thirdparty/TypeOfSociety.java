package com.data4help.d4h_thirdparty;

public enum TypeOfSociety {
    SPA, SRL, SNC, SAS, SAA;


    public String toString(){
        switch(this){
            case SPA:
                return "spa";
            case SRL:
                return "srl";
            case SNC:
                return "snc";
            case SAS:
                return "sas";
            case SAA:
                return "saa";
        }
        return null;
    }

    public static boolean evaluate(String sigla){
        return sigla.equalsIgnoreCase(SPA.toString()) || sigla.equalsIgnoreCase(SRL.toString()) || sigla.equalsIgnoreCase(SNC.toString()) ||
                sigla.equalsIgnoreCase(SAS.toString()) || sigla.equalsIgnoreCase(SAA.toString());
    }
}

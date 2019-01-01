package com.d4h.application.model.services;

import javax.ejb.Singleton;

@Singleton
public class SubscribeService {
    private static SubscribeService service;

    private SubscribeService(){}

    public static SubscribeService getService(){
        if(service == null)
            service = new SubscribeService();
        return service;
    }
}

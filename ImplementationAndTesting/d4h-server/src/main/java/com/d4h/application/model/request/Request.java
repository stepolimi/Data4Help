package com.d4h.application.model.request;

import com.d4h.application.model.thirdParty.ThirdParty;

public interface Request {

    void setAccepted(boolean accepted);
    boolean isAccepted();


    void setSender(ThirdParty sender);
    ThirdParty getSender();

    String getId();

    void setId(String id);
}

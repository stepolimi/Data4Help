package com.d4h.application.model.request;

import com.d4h.application.model.thirdParty.ThirdParty;

/**
 * Interface for user data requests and group of users data requests.
 */
public interface Request {

    void setAccepted(boolean accepted);
    boolean isAccepted();


    void setSender(ThirdParty sender);
    ThirdParty getSender();

    String getId();

    void setId(String id);
}

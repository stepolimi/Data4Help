package com.d4h.application.model.request;

/**
 * Information about a user request to be sent to the interested user.
 */
public class RequestUserSent {
    private String senderName;
    private String description;
    private String requestId;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
}

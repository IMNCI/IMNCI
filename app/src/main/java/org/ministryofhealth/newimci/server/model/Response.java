package org.ministryofhealth.newimci.server.model;

/**
 * Created by chriz on 1/3/2018.
 */

public class Response {
    private boolean status;
    private String message;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

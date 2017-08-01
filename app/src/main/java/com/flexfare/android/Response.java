package com.flexfare.android;

import java.io.Serializable;

/**
 * Created by Blessyn on 8/1/2017.
 */

public class Response implements Serializable {

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String status;
    private String message;

    public Response(){

    }
}

package com.linzhi.model;

import java.io.Serializable;

/**
 * Created by sjy on 2017/4/12.
 */

public class VipSearchListModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ClientID;
    private String ClientName;
    private String ClientGender;
    private String ClientLevel;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClientID() {
        return ClientID;
    }

    public void setClientID(String clientID) {
        ClientID = clientID;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientGender() {
        return ClientGender;
    }

    public void setClientGender(String clientGender) {
        ClientGender = clientGender;
    }

    public String getClientLevel() {
        return ClientLevel;
    }

    public void setClientLevel(String clientLevel) {
        ClientLevel = clientLevel;
    }
}

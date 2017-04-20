package com.linzhi.model;

import java.io.Serializable;

/**
 * 详情model
 * Created by sjy on 2017/4/13.
 */

public class DetailModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String ClientID;
    private String ClientName;
    private String ClientGender;
    private String IDCardNo;
    private String ClientLevel;
    private String Remark;
    private String ActiveFlag;
    private String CreateTime;
    private String ImgPath;
    private String clientPhone;

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

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

    public String getIDCardNo() {
        return IDCardNo;
    }

    public void setIDCardNo(String IDCardNo) {
        this.IDCardNo = IDCardNo;
    }

    public String getClientLevel() {
        return ClientLevel;
    }

    public void setClientLevel(String clientLevel) {
        ClientLevel = clientLevel;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getActiveFlag() {
        return ActiveFlag;
    }

    public void setActiveFlag(String activeFlag) {
        ActiveFlag = activeFlag;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getImgPath() {
        return ImgPath;
    }

    public void setImgPath(String imgPath) {
        ImgPath = imgPath;
    }
}

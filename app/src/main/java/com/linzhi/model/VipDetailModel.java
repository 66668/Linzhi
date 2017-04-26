package com.linzhi.model;

import java.io.Serializable;

/**
 * vip查询详情model
 * Created by sjy on 2017/4/13.
 */

public class VipDetailModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientID; //客户号
    private String clientName;//客户名字
    private String clientPhone;//客户手机号
    private String clientGender;//性别  1男 2女
    private String IDCardNo;//身份证号
    private String clientLevel;//客户等级
    private String remark;//备注
    private String createTime;//创建时间
    private String path;//图片路径

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhone() {
        return clientPhone;
    }

    public void setClientPhone(String clientPhone) {
        this.clientPhone = clientPhone;
    }

    public String getClientGender() {
        return clientGender;
    }

    public void setClientGender(String clientGender) {
        this.clientGender = clientGender;
    }

    public String getIDCardNo() {
        return IDCardNo;
    }

    public void setIDCardNo(String IDCardNo) {
        this.IDCardNo = IDCardNo;
    }

    public String getClientLevel() {
        return clientLevel;
    }

    public void setClientLevel(String clientLevel) {
        this.clientLevel = clientLevel;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

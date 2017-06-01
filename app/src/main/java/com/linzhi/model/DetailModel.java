package com.linzhi.model;

import java.io.Serializable;
import java.util.List;

/**
 * 详情model
 * Created by sjy on 2017/4/13.
 */

public class DetailModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String clientID;
    private String clientName;
    private String clientGender;
    private String IDCardNo;
    private String clientLevel;
    private String remark;
    private String activeFlag;
    private String createTime;
    private String clientPhone;
    private String imgPath;
    private List<CarModel> carNumList;

    public List<CarModel> getCarNumList() {
        return carNumList;
    }

    public void setCarNumList(List<CarModel> carNumList) {
        this.carNumList = carNumList;
    }

    public class CarModel implements Serializable {
        private static final long serialVersionUID = 2L;
        private String infoID;
        private String carNum;

        public String getInfoID() {
            return infoID;
        }

        public void setInfoID(String infoID) {
            this.infoID = infoID;
        }

        public String getCarNum() {
            return carNum;
        }

        public void setCarNum(String carNum) {
            this.carNum = carNum;
        }
    }

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

    public String getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(String activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}

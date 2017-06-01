package com.linzhi.db.entityimpl;


import com.linzhi.db.entity.EntityBase;

/**
 * LoginActivity--调用ConfigUtil--调用该方法
 *
 * @author JackSong
 */
public class UserEntity extends EntityBase {
    //
    public UserTable TableSchema() {
        return (UserTable) _tableSchema;
    }

    //构造方法赋值
    public UserEntity() {
        //赋值
        _tableSchema = UserTable.Current();
    }


    public String getPassword() {
        return (String) GetData(UserTable.C_Password);
    }

    public void setPassword(String value) {
        SetData(UserTable.C_Password, value);
    }

    public String getUserName() {
        return (String) GetData(UserTable.C_userName);
    }

    public void setUserName(String value) {
        SetData(UserTable.C_userName, value);

    }

    //保存网点号
    public String getSiteID() {
        return (String) GetData(UserTable.C_SiteID);
    }

    public void setSiteID(String value) {
        SetData(UserTable.C_SiteID, value);

    }


}

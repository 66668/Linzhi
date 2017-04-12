package com.linzhi.db.entity;

/**
 * 数据存储实体类，通过该类与外部联系
 *
 * 该存储框架 通过UerEntity-get/set方法,将数值存储到hashmap中，
 * 最终，entity又被ConfigUtil保存到sp中调用
 */
public class UserEntity extends EntityBase {

    public UserTable TableSchema() {
        return (UserTable) _tableSchema;
    }

    public UserEntity() {
        _tableSchema = UserTable.Current();
    }

    //
    public String getEmployeeID() {
        return (String) GetData(UserTable.C_EmployeeID);
    }

    public void setEmployeeID(String value) {
        SetData(UserTable.C_EmployeeID, value);
    }

    //
    public String getPassword() {
        return (String) GetData(UserTable.C_Password);
    }

    public void setPassword(String value) {
        SetData(UserTable.C_Password, value);
    }

    //
    public void setStoreID(String value) {
        SetData(UserTable.C_StoreID, value);
    }

    public String getStoreID() {
        return (String) GetData(UserTable.C_StoreID);
    }

    //
    public void setstoreId(String value) {
        SetData(UserTable.C_storeId, value);
    }

    public String getstoreId() {
        return (String) GetData(UserTable.C_storeId);
    }

    //
    public String getStoreUserId() {
        return (String) GetData(UserTable.C_StoreUserId);
    }

    public void setStoreUserId(String value) {
        SetData(UserTable.C_StoreUserId, value);
    }

    //
    public String getName() {
        return (String) GetData(UserTable.C_Name);
    }

    public void setName(String value) {
        SetData(UserTable.C_Name, value);
    }

    //
    public String getTelephone() {
        return (String) GetData(UserTable.C_Telephone);
    }

    public void setTelephone(String value) {
        SetData(UserTable.C_Telephone, value);
    }

    //
    public String getEmail() {
        return (String) GetData(UserTable.C_Email);
    }

    public void setEmail(String value) {
        SetData(UserTable.C_Email, value);
    }

    //
    public String getJobNumber() {
        return (String) GetData(UserTable.C_JobNumber);
    }

    public void setJobNumber(String value) {
        SetData(UserTable.C_JobNumber, value);
    }

    //
    public String getStoreName() {
        return (String) GetData(UserTable.C_StoreName);
    }

    public void setStoreName(String value) {
        SetData(UserTable.C_StoreName, value);
    }

    //
    public String getDepartmentName() {
        return (String) GetData(UserTable.C_DepartmentName);
    }

    public void setDepartmentName(String value) {
        SetData(UserTable.C_DepartmentName, value);
    }

    //
    public String getPostName() {
        return (String) GetData(UserTable.C_PostName);
    }

    public void setPostName(String value) {
        SetData(UserTable.C_PostName, value);
    }

    //
    public String getEntryDate() {
        return (String) GetData(UserTable.C_EntryDate);
    }

    public void setEntryDate(String value) {
        SetData(UserTable.C_EntryDate, value);
    }

    //
    public String getUserPicture() {
        return (String) GetData(UserTable.C_UserPicture);
    }

    public void setUserPicture(String value) {
        SetData(UserTable.C_UserPicture, value);
    }

    //
    public String getWorlkId() {
        return (String) GetData(UserTable.C_WorkId);
    }

    public void setWorkId(String value) {
        SetData(UserTable.C_WorkId, value);
    }
}

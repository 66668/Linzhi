package com.linzhi.db.entityimpl;


import com.linzhi.db.entity.ColumnInfo;
import com.linzhi.db.entity.TableBase;

/**
 * 数据存储，本质上，是将参数全部保存到HasnMap中，hashMap的value是对象ColumnInfo，一个ColumnInfo保存一个参数，并对参数做具体分析
 */
public class UserTable extends TableBase {
    protected static UserTable _current;

    public static String C_TableName = "user";

    public static String C_userName = "userName";
    public static String C_Password = "password";
    public static String C_SiteID = "siteid";


    public UserTable() {
        _tableName = C_TableName;
    }

    public static UserTable Current() {
        if (_current == null) {
            Initial();
        }
        return _current;
    }

    private static void Initial() {
        //创建该表,同时该表的参数，实质上全部以hashMap方式保存
        _current = new UserTable();

        _current.Add(C_userName, new ColumnInfo(C_userName, "userName", false, "String"));
        _current.Add(C_Password, new ColumnInfo(C_Password, "Password", false, "String"));
        _current.Add(C_SiteID, new ColumnInfo(C_SiteID, "siteid", false, "String"));
    }


    //重写父类方法 GetColumnInfoByName，返回放入表格的信息，外部未调用


    //
    public ColumnInfo Password() {
        return GetColumnInfoByName(C_Password);
    }

    //
    public ColumnInfo userName() {
        return GetColumnInfoByName(C_userName);
    }

    //
    public ColumnInfo siteID() {
        return GetColumnInfoByName(C_SiteID);
    }


}

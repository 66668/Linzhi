package com.linzhi.db.entityimpl;


import com.linzhi.db.entity.ColumnInfo;
import com.linzhi.db.entity.TableInfo;

/**
 *  用户的数据存储
 */
public class UserTable extends TableInfo {
    protected static UserTable _current;

    public static String C_TableName = "user";

    public static String C_userName = "userName";
    public static String C_passWord = "password";


    public UserTable() {
        _tableName = "user";
    }

    public static UserTable Current() {
        if (_current == null) {
            Initial();
        }
        return _current;
    }

    private static void Initial() {
        _current = new UserTable();
        _current.Add(C_userName, new ColumnInfo(C_userName, "userName", false, "String"));
        _current.Add(C_passWord, new ColumnInfo(C_passWord, "passWord", false, "String"));
    }


    //
    public ColumnInfo userName() {
        return GetColumnInfoByName(C_userName);
    }

    //
    public ColumnInfo passWord() {
        return GetColumnInfoByName(C_passWord);

    }


}

package com.linzhi.db.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 */
public class TableBase implements Serializable {

    public HashMap<String, ColumnInfo> tableMap;
    protected String _tableName;

    public String GetTableName() {
        return _tableName;
    }

    public TableBase() {
        tableMap = new HashMap<String, ColumnInfo>();
    }

    //添加一个对象
    public void Add(String columnName, ColumnInfo column) {
        SetColumnInfoByName(column, columnName);
    }

    //获取一个对象
    public ColumnInfo GetColumnInfoByName(String columnName) {
        return tableMap.get(columnName);
    }

    public void SetColumnInfoByName(ColumnInfo value, String columnName) {
        tableMap.put(columnName, value);
    }

    protected ArrayList<ColumnInfo> _allColumnInfo;

    //获取所有参数，将arraymap转成arrayList形式
    public ArrayList<ColumnInfo> GetAllColumnInfo() {
        if (_allColumnInfo == null) {
            _allColumnInfo = new ArrayList<ColumnInfo>();
            for (ColumnInfo ci : tableMap.values()) {
                _allColumnInfo.add(ci);
            }
        }
        return _allColumnInfo;

    }

    protected ArrayList<String> _allColumnName;

    //获取所有参数名，保存成arrayList
    public ArrayList<String> GetAllColumnName() {

        if (_allColumnName == null) {
            _allColumnName = new ArrayList<String>();
            for (ColumnInfo ci : tableMap.values()) {
                _allColumnName.add(ci.ColumnName);
            }
        }
        return _allColumnName;

    }

    protected ArrayList<ColumnInfo> _keyColumnInfo;

    //获取主键值，保存成arrayList
    public ArrayList<ColumnInfo> GetKeyColumnInfo() {

        if (_keyColumnInfo == null) {
            _keyColumnInfo = new ArrayList<ColumnInfo>();
            for (ColumnInfo ci : tableMap.values()) {
                if (ci.IsPrimaryKey) {
                    _keyColumnInfo.add(ci);
                }
            }
        }
        return _keyColumnInfo;

    }

    protected ArrayList<ColumnInfo> _valueColumnInfo;

    //获取非主键值，保存成arrayList
    public ArrayList<ColumnInfo> GetValueColumnInfo() {
        if (_valueColumnInfo == null) {
            _valueColumnInfo = new ArrayList<ColumnInfo>();
            for (ColumnInfo ci : tableMap.values()) {
                if (!ci.IsPrimaryKey) {
                    _valueColumnInfo.add(ci);
                }
            }
        }
        return _valueColumnInfo;
    }
}

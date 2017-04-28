package com.linzhi.db.entity;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class EntityBase {

    protected TableBase _tableSchema;

    public TableBase GetOringTableSchema() {
        return _tableSchema;
    }

    protected HashMap<String, Object> _data;

    public HashMap<String, Object> GetDataCollection() {
        return _data;
    }
    public EntityBase() {
        _data = new HashMap<String, Object>();
    }



    public void SetData(String key, Object value) {
        if (value != null) {
            _data.put(key.trim(), value);
        }
    }

    public Object GetData(String key) {
        return _data.get(key.trim());
    }

    public ArrayList<String> GetKeys() {
        return GetOringTableSchema().GetAllColumnName();
    }




    /**
     * translate json to entity
     *
     * @param <T>
     * @param item
     * @param c
     * @return
     * @throws JSONException
     */
    public static <T extends EntityBase> EntityBase toEntityBase(
            JSONObject item, Class<T> c) throws JSONException {
        //EntityBase obj = null;
        T obj = null;
        try {
            obj = c.newInstance();
            TableBase table = obj.GetOringTableSchema();
            //String tableName = table.GetTableName();
            //obj = DataAccessBroker.createEntityBase(tableName);
            ArrayList<ColumnInfo> cs = table.GetAllColumnInfo();
            for (ColumnInfo colInfo : cs) {
                if (colInfo.DataType.equalsIgnoreCase("int") || colInfo.DataType.equalsIgnoreCase("Integer")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getInt(colInfo.ColumnName));
                } else if (colInfo.DataType.equalsIgnoreCase("long")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getLong(colInfo.ColumnName));
                } else if (colInfo.DataType.equalsIgnoreCase("double")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getDouble(colInfo.ColumnName));
                } else if (colInfo.DataType.equalsIgnoreCase("string")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getString(colInfo.ColumnName));
                } else if (colInfo.DataType.equalsIgnoreCase("datetime")) {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getString(colInfo.ColumnName));
                } else {
                    if (item.has(colInfo.ColumnName))
                        obj.SetData(colInfo.ColumnName,
                                item.getString(colInfo.ColumnName));
                }
            }
        } catch (IllegalAccessException illex) {
            Log.d("SJY", illex.getMessage());
        } catch (InstantiationException inex) {
            Log.d("SJY", inex.getMessage());
        }
        return obj;
    }

    //configUtils setUserEntity使用
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        for (ColumnInfo column : _tableSchema.GetAllColumnInfo()) {
            Object obj = GetData(column.ColumnName);
            try {
                jsonObject.put(column.ColumnName, obj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }
}

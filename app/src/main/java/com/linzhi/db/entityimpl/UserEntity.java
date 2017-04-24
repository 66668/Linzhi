package com.linzhi.db.entityimpl;

import com.linzhi.db.entity.EntityBase;

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
}

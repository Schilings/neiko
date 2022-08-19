package com.schilings.neiko.admin.upms.config.mybatis;


import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

public class FillMetaObjectHandle implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        // 逻辑删除标识
        if (metaObject.hasSetter("deleted")) {
            this.strictInsertFill(metaObject, "deleted", Long.class, GlobalConstants.NOT_DELETED_FLAG);
        }
        // 创建时间
        if (metaObject.hasSetter("createTime")) {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 修改时间
        if (metaObject.hasSetter("updateTime")) {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
        }
    }
}

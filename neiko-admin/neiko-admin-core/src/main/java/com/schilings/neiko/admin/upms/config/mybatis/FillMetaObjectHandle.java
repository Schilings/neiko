package com.schilings.neiko.admin.upms.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.schilings.neiko.authorization.common.userdetails.User;
import com.schilings.neiko.authorization.common.util.SecurityUtils;
import com.schilings.neiko.common.model.constants.GlobalConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;

/**
 *
 * <p>
 * MP自动填充策略
 * </p>
 *
 * @author Schilings
 */
@Slf4j
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
		// 创建人
		User user = SecurityUtils.getUser();
		if (metaObject.hasSetter("createBy")) {
			try {
				this.strictInsertFill(metaObject, "createBy", Long.class, user.getUserId());
			}
			catch (Exception e) {
				this.strictInsertFill(metaObject, "createBy", Long.class, null);
				// log.error("[strictInsertFill]createBy insert error!,ex:" + e);
			}
		}
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// 修改时间
		if (metaObject.hasSetter("updateTime")) {
			this.strictUpdateFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
		}
		// 修改人
		User user = SecurityUtils.getUser();
		if (metaObject.hasSetter("updateBy")) {
			try {
				this.strictInsertFill(metaObject, "updateBy", Long.class, user.getUserId());
			}
			catch (Exception e) {
				this.strictInsertFill(metaObject, "updateBy", Long.class, null);
				// log.error("[strictInsertFill]updateBy insert error!,ex:" + e);
			}
		}
	}

}

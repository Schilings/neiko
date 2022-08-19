package com.schilings.neiko.log.converter;


import com.schilings.neiko.log.model.entity.AccessLog;
import com.schilings.neiko.log.model.vo.AccessLogPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 访问日志
 *
 * @author hccake 2021-03-22 20:23:41
 */
@Mapper
public interface AccessLogConverter {

	AccessLogConverter INSTANCE = Mappers.getMapper(AccessLogConverter.class);

	/**
	 * PO 转 PageVO
	 * @param accessLog 访问日志
	 * @return AdminAccessLogVO 访问日志VO
	 */
	AccessLogPageVO poToPageVo(AccessLog accessLog);

}

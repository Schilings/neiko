package com.schilings.neiko.samples.extend.mybatis.plus.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.schilings.neiko.samples.extend.mybatis.plus.entity.PodRefOutordProps;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
public class EntirePreorders implements Serializable {

	private static final long serialVersionUID = 1L;

	private String preorderId;

	private String channelCode;

	private String driverId;

	private String gasStationId;

	private String waybillId;

	private BigDecimal amount;

	private String status;

	private String driverName;

	private String orderType;

	private String contactMobile;

	private String licensePlateNumber;

	private String cardType;

	private LocalDateTime expiryDate;

	private Integer deleted;

	private String createdById;

	private String updatedById;

	private LocalDateTime createdDatetime;

	private LocalDateTime updatedDatetime;

	// ========================================
	@TableField("podRefId_PRO")
	private String podRefId_PRO;

	private String outOrderType;

	private String outOrderCode;

	@TableField("preorderId_PRO")
	private String preorderId_PRO;

	@TableField("status_PRO")
	private String status_PRO;

	// =============================================

	/**
	 * 外部属性
	 */
	@TableField(exist = false)
	List<PodRefOutordProps> props;

}

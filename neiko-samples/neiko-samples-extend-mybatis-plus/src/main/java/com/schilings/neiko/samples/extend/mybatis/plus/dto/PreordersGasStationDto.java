package com.schilings.neiko.samples.extend.mybatis.plus.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreordersGasStationDto {

	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
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

	private String brandCode;

	private String gasStationName;
	

}

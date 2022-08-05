package com.schilings.neiko.samples.extend.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


/**
 * 油站表
 * 
 * @author newrain
 * @date 2022-03-17 18:17:25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@EqualsAndHashCode
@TableName("gas_station")
public class GasStation implements Serializable {
	
	/**
	 * 主键
	 */
	@TableId(type = IdType.AUTO)
	private String id;


	private String code;

	private String name;


	private String brandCode;


	private String supplierName;


	private String detailAddress;


	private BigDecimal latitude;

	private BigDecimal longitude;

	private String mobile;


	private String visOilFlag;


	private String phyCardNo;
	

	private String state;


	private String businessTime;


	private Date signingTime;


	private LocalDateTime createdDatetime;


	private LocalDateTime updatedDatetime;

	private Integer deleted;

	private BigDecimal agreementRatio;
	

}

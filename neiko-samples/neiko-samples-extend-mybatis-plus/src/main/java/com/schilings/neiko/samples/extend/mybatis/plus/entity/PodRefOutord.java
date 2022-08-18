package com.schilings.neiko.samples.extend.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * <pre>
 * <p>@ApiModel("预约单-外部预约单信息表")</p>
 * </pre>
 *
 * @author Schilings
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pod_ref_outord")
public class PodRefOutord implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@TableId(type = IdType.AUTO)
	private String podRefId;

	private String preorderId;

	private String outOrderType;

	private String outOrderCode;

	private String status;

}

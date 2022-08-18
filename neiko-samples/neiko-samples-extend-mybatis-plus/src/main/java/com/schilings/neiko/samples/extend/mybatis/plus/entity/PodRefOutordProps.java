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
 * <p>预约单-外部预约单额外信息表</p>
 * </pre>
 *
 * @author Schilings
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("pod_ref_outord_props")
public class PodRefOutordProps implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键Id
	 */
	@TableId(type = IdType.AUTO)
	private String podOutOrdPropsId;

	private String podRefId;

	/**
	 * 属性名
	 */
	private String propName;

	private String propType;

	private String valVarchar;

	private String valClob;

}

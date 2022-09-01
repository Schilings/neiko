package com.schilings.neiko.wechat.model.entity;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.schilings.neiko.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 
 * <p>微信公众号自定义模板消息</p>
 * 
 * @author Schilings
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(title = "微信公众号自定义模板消息")
@TableName("nk_wechat_mp_template")
public class WechatMpTemplate extends LogicDeletedBaseEntity {

    private static final long serialVersionUID = 1L;

    /** 模板id */
    @TableId
    private Long id;


    /** 模板编号 */
    private String tempkey;


    /** 模板名 */
    private String name;


    /** 回复内容 */
    private String content;


    /** 模板ID */
    private String tempid;


    /** 状态 */
    private Integer status;


}

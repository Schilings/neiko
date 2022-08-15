package com.schilings.neiko.samples.extend.mybatis.plus.entity;




import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.schilings.neiko.extend.mybatis.plus.method.JoinResultType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <pre>{@code
 *      
 * }
 * <p>预约单表</p>
 * </pre>
 * @author Schilings
*/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("preorders")
public class Preorders extends JoinResultType implements Serializable {

    private static final long serialVersionUID = 1L;
    
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

}

package com.schilings.neiko.extend.mybatis.plus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.injector.methods.InsertBatchSomeColumn;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.extend.mybatis.plus.constants.Constant;
import com.schilings.neiko.extend.mybatis.plus.utils.PageUtil;
import com.schilings.neiko.extend.mybatis.plus.wrapper.join.NeikoBaseJoin;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * <pre>
 * <p>拓展Mapper接口，用于自定义方法</p>
 * <p>所有的 Mapper接口 都需要继承当前接口 如果想自己定义其他的全局方法， 您的全局 BaseMapper 需要继承当前接口</p>
 * </pre>
 * @author Schilings
*/
public interface ExtendMapper<T> extends BaseMapper<T> {

    /**
     * 根据 PageParam 生成一个 IPage 实例
     * @param pageParam 分页参数
     * @param <V> 返回的 Record 对象
     * @return IPage<V>
     */
    default <V> IPage<V> prodPage(PageParam pageParam) {
        return PageUtil.prodPage(pageParam);
    }

    /**
     * 批量插入数据 实现类 {@link InsertBatchSomeColumn}
     * @param list 数据列表
     * @return int 改动行
     */
    int insertBatchSomeColumn(@Param("collection") Collection<T> list);


    /**
     * 链表查询
     * @param clazz
     * @param resultMap 如果不指定ResultMap，那么根据DTO的Class类型自动生成，字段名驼峰转为列名，如ordPRO-->ord_p_r_o,
     *                  所以如果wrapper使用selectAs别名查询，那么可以DTO对应字段使用@TableField指定列名
     * @param wrapper
     * @param <DTO>
     * @return
     */

    <DTO> List<DTO> selectJoinList(@Param(Constant.CLAZZ) Class<DTO> clazz,
                                   @Param(Constant.RESULT_MAP) String resultMap,
                                   @Param(Constants.WRAPPER) NeikoBaseJoin wrapper);


    /**
     * 连表查询返回记录集合并分页
     *
     * @param wrapper joinWrapper
     * @param clazz   resultType
     * @param <DTO>   分页返回对象
     */
    <DTO, P extends IPage<?>> IPage<DTO> selectJoinPage(P page,
                                                        @Param(Constant.CLAZZ) Class<DTO> clazz,
                                                        @Param(Constant.RESULT_MAP) String resultMap,
                                                        @Param(Constants.WRAPPER) NeikoBaseJoin wrapper);

    /**
     * 连表查询返回Map集合
     *
     * @param wrapper joinWrapper
     */
    List<Map<String, Object>> selectJoinMapsList(@Param(Constants.WRAPPER) NeikoBaseJoin wrapper);

    
    /**
     * 连表查询返回Map集合并分页
     *
     * @param wrapper joinWrapper
     */
    <P extends IPage<?>> IPage<Map<String, Object>> selectJoinMapsPage(P page,
                                                                       @Param(Constants.WRAPPER) NeikoBaseJoin wrapper);
    
    
}

package com.schilings.neiko.wechat.service;

import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.ExtendService;
import com.schilings.neiko.wechat.model.dto.WechatOpenDataDTO;
import com.schilings.neiko.wechat.model.entity.WechatUser;
import com.schilings.neiko.wechat.model.qo.WechatUserQO;
import com.schilings.neiko.wechat.model.vo.WechatUserPageVO;
import me.chanjar.weixin.common.error.WxErrorException;

public interface WechatUserService extends ExtendService<WechatUser> {

    /**
     * 根据QueryObject查询分页数据
     * @param pageParam 分页参数
     * @param qo 查询参数对象
     * @return 分页数据
     */
    PageResult<WechatUserPageVO> queryPage(PageParam pageParam, WechatUserQO qo);


    /**
     * 同步微信用户
     * @throws WxErrorException
     */
    void synchroUser() throws WxErrorException;

    /**
     * 根据Id获取用户
     * @param openId
     * @return
     */
    WechatUser getById(Long id);

    /**
     * 根据openId获取用户
     * @param openId
     * @return
     */
    WechatUser getByOpenId(String openId);

    /**
     * 新增、更新微信用户
     * @param wxOpenDataDTO
     * @return
     */
    WechatUser saveOrUptateWxUser(WechatOpenDataDTO wxOpenDataDTO);
}

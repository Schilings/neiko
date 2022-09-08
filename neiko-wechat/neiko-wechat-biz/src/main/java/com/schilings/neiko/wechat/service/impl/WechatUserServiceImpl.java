package com.schilings.neiko.wechat.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.google.common.collect.Lists;
import com.schilings.neiko.common.model.domain.PageParam;
import com.schilings.neiko.common.model.domain.PageResult;
import com.schilings.neiko.extend.mybatis.plus.service.impl.ExtendServiceImpl;
import com.schilings.neiko.wechat.mapper.WechatUserMapper;
import com.schilings.neiko.wechat.service.WechatUserService;
import com.schilings.neiko.wechat.constant.WechatConst;
import com.schilings.neiko.wechat.model.dto.WechatOpenDataDTO;
import com.schilings.neiko.wechat.model.entity.WechatUser;
import com.schilings.neiko.wechat.model.qo.WechatUserQO;
import com.schilings.neiko.wechat.model.vo.WechatUserPageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatUserServiceImpl extends ExtendServiceImpl<WechatUserMapper, WechatUser>
		implements WechatUserService {

	private final WxMpService wxMpService;

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public PageResult<WechatUserPageVO> queryPage(PageParam pageParam, WechatUserQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 同步微信用户
	 * @throws WxErrorException
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void synchroUser() throws WxErrorException {
		// 先将已关注的用户取关
		WechatUser wxUser = new WechatUser();
		wxUser.setSubscribe(WechatConst.Subscribe.NO.getValue());
		this.baseMapper.update(wxUser,
				Wrappers.<WechatUser>lambdaQuery().eq(WechatUser::getSubscribe, WechatConst.Subscribe.YES.getValue()));
		WxMpUserService wxMpUserService = wxMpService.getUserService();
		this.recursionGet(wxMpUserService, null);
	}

	/**
	 * 根据Id获取用户
	 * @param id
	 * @return
	 */
	@Override
	public WechatUser getById(Long id) {
		return baseMapper.getById(id);
	}

	/**
	 * 根据openId获取用户
	 * @param openId
	 * @return
	 */
	@Override
	public WechatUser getByOpenId(String openId) {
		return baseMapper.getByOpenId(openId);
	}

	/**
	 * 新增、更新微信用户
	 * @param wxOpenDataDTO
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public WechatUser saveOrUptateWxUser(WechatOpenDataDTO wxOpenDataDTO) {
		return null;
	}

	/**
	 * 递归获取
	 * @param nextOpenid
	 */
	void recursionGet(WxMpUserService wxMpUserService, String nextOpenid) throws WxErrorException {
		WxMpUserList userList = wxMpUserService.userList(nextOpenid);
		List<WechatUser> listWxUser = new ArrayList<>();
		List<WxMpUser> listWxMpUser = getWxMpUserList(wxMpUserService, userList.getOpenids());
		listWxMpUser.forEach(wxMpUser -> {
			WechatUser wxUser = baseMapper
					.selectOne(Wrappers.<WechatUser>lambdaQuery().eq(WechatUser::getOpenId, wxMpUser.getOpenId()));
			if (wxUser == null) {// 用户未存在
				wxUser = new WechatUser();
				wxUser.setSubscribeNum(1);
			}
			//SubscribeHandler.setWxUserValue(wxUser, wxMpUser);
			listWxUser.add(wxUser);
		});
		this.saveOrUpdateBatch(listWxUser);
		if (userList.getCount() >= 10000) {
			this.recursionGet(wxMpUserService, userList.getNextOpenid());
		}
	}

	/**
	 * 分批次获取微信粉丝信息 每批100条
	 * @param wxMpUserService
	 * @param openidsList
	 * @return
	 * @throws WxErrorException
	 * @author
	 */
	private List<WxMpUser> getWxMpUserList(WxMpUserService wxMpUserService, List<String> openidsList)
			throws WxErrorException {
		// 粉丝openid数量
		int count = openidsList.size();
		if (count <= 0) {
			return new ArrayList<>();
		}
		List<WxMpUser> list = Lists.newArrayList();
		List<WxMpUser> followersInfoList;
		int a = count % 100 > 0 ? count / 100 + 1 : count / 100;
		for (int i = 0; i < a; i++) {
			if (i + 1 < a) {
				log.debug("i:{},from:{},to:{}", i, i * 100, (i + 1) * 100);
				followersInfoList = wxMpUserService.userInfoList(openidsList.subList(i * 100, ((i + 1) * 100)));
				if (null != followersInfoList && !followersInfoList.isEmpty()) {
					list.addAll(followersInfoList);
				}
			}
			else {
				log.debug("i:{},from:{},to:{}", i, i * 100, count - i * 100);
				followersInfoList = wxMpUserService.userInfoList(openidsList.subList(i * 100, count));
				if (null != followersInfoList && !followersInfoList.isEmpty()) {
					list.addAll(followersInfoList);
				}
			}
		}
		log.debug("本批次获取微信粉丝数：", list.size());
		return list;
	}

}

package com.schilings.neiko.notify.websocket;

import cn.hutool.core.collection.CollUtil;

import com.schilings.neiko.common.util.json.JsonUtils;
import com.schilings.neiko.common.websocket.distribute.MessageDistributor;
import com.schilings.neiko.common.websocket.distribute.dto.MessageDTO;
import com.schilings.neiko.common.websocket.message.JsonWebSocketMessage;
import com.schilings.neiko.notify.model.domain.NotifyInfo;
import com.schilings.neiko.system.model.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * <p>基类：将NotifyInfo转换成JsonWebsocketMessage进行Websocket通知</p>
 * 
 * @author Schilings
*/
public abstract class AbstractNotifyInfoHandler<T extends NotifyInfo, M extends JsonWebSocketMessage>
		implements NotifyInfoHandler<T> {

	@Autowired
	private MessageDistributor messageDistributor;

	protected final Class<T> clz;

	@SuppressWarnings("unchecked")
	protected AbstractNotifyInfoHandler() {
		Type superClass = getClass().getGenericSuperclass();
		ParameterizedType type = (ParameterizedType) superClass;
		clz = (Class<T>) type.getActualTypeArguments()[0];
	}

	@Override
	public void handle(List<SysUser> userList, T notifyInfo) {
		M message = createMessage(notifyInfo);
		String msg = JsonUtils.toJson(message);
		List<Object> sessionKeys = userList.stream().map(SysUser::getUserId).collect(Collectors.toList());
		persistMessage(userList, notifyInfo);
		MessageDTO messageDO = new MessageDTO()
				.setMessageText(msg)
				.setSessionKeys(sessionKeys)
				.setNeedBroadcast(CollUtil.isEmpty(sessionKeys));
		messageDistributor.distribute(messageDO);
	}

	@Override
	public Class<T> getNotifyClass() {
		return this.clz;
	}

	/**
	 * 持久化通知
	 * @param userList 通知用户列表
	 * @param notifyInfo 消息内容
	 */
	protected abstract void persistMessage(List<SysUser> userList, T notifyInfo);

	/**
	 * 产生推送消息
	 * @param notifyInfo 消息内容
	 * @return 分发消息
	 */
	protected abstract M createMessage(T notifyInfo);

}

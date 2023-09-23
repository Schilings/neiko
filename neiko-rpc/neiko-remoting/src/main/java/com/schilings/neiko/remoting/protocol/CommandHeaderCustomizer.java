package com.schilings.neiko.remoting.protocol;

import com.schilings.neiko.remoting.exception.RemotingCommandException;

/**
 *
 * <p>
 * 消息头自定义处理器
 * </p>
 * <p>
 * 实现类的自定义字段作为请求头信息
 * </p>
 *
 * @author Schilings
 */
public interface CommandHeaderCustomizer {

	void checkFields() throws RemotingCommandException;

}

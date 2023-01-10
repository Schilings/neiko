package com.schilings.neiko.remoting;

import com.schilings.neiko.remoting.protocol.RemotingCommand;

/**
 *
 * <p>
 * RPC钩子
 * </p>
 *
 * @author Schilings
 */
public interface RPCHook {

	void doBeforeRequest(final String remoteAddr, final RemotingCommand request);

	void doAfterResponse(final String remoteAddr, final RemotingCommand request, final RemotingCommand response);

}

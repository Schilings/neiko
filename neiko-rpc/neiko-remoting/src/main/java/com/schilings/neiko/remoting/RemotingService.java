package com.schilings.neiko.remoting;

/**
 *
 * <p>
 * 远程服务
 * </p>
 * <p>
 * 顶层接口
 * </p>
 *
 * @author Schilings
 */
public interface RemotingService {

	void start();

	void shutdown();

	void registerRPCHook(RPCHook rpcHook);

}

package com.schilings.neiko.common.netty.core;

/**
 *
 * <p>
 * 通用事件处理程序。
 * </p>
 *
 * @author Schilings
 */
@FunctionalInterface
public interface Handler<E> {

	void handle(E event);

}

package com.schilings.neiko.system.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 字典修改事件
 */
@Getter
@ToString
public class DictChangeEvent extends ApplicationEvent {

	private final String dictCode;

	public DictChangeEvent(String dictCode) {
		super(dictCode);
		this.dictCode = dictCode;
	}

}

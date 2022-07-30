package com.schilings.neiko.common.event.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompositeEventHandle implements EventHandler {

	private List<EventHandler> handlers;

	@Override
	public void handle(Object event) {

	}

}

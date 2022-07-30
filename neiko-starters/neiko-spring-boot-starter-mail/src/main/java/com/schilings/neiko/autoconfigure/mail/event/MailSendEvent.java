package com.schilings.neiko.autoconfigure.mail.event;

import com.schilings.neiko.autoconfigure.mail.model.MailSendInfo;
import org.springframework.context.ApplicationEvent;

public class MailSendEvent extends ApplicationEvent {

	public MailSendEvent(MailSendInfo source) {
		super(source);
	}

}

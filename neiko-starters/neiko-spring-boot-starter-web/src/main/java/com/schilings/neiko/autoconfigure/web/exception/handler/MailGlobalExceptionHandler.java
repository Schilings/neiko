package com.schilings.neiko.autoconfigure.web.exception.handler;

import com.schilings.neiko.autoconfigure.mail.model.MailSendInfo;
import com.schilings.neiko.autoconfigure.mail.sender.MailSender;
import com.schilings.neiko.autoconfigure.web.exception.ExceptionHandleProperties;
import com.schilings.neiko.autoconfigure.web.exception.domain.ExceptionMessage;
import com.schilings.neiko.autoconfigure.web.exception.domain.ExceptionNoticeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * <pre>
 * <p>异常邮件通知</p>
 * </pre>
 *
 * @author Schilings
 */
@Slf4j
public class MailGlobalExceptionHandler extends AbstractNoticeGlobalExceptionHandler {

	private final MailSender sender;

	public MailGlobalExceptionHandler(ExceptionHandleProperties config, MailSender sender, String applicationName) {
		super(config, applicationName);
		this.sender = sender;
	}

	@Override
	public ExceptionNoticeResponse send(ExceptionMessage sendMessage) {
		String[] to = config.getReceiveEmails().toArray(new String[0]);
		log.info("发生异常警告邮件通知,to:{}", to);
		MailSendInfo mailSendInfo = sender.sendTextMail("异常警告", sendMessage.toString(), to);
		// 邮箱发送失败会抛出异常，否则视作发送成功
		return new ExceptionNoticeResponse().setSuccess(mailSendInfo.getSuccess())
				.setErrMsg(mailSendInfo.getErrorMsg());
	}

}

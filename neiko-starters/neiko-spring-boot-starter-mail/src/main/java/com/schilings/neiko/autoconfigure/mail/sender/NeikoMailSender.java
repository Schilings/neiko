package com.schilings.neiko.autoconfigure.mail.sender;

import com.schilings.neiko.autoconfigure.mail.event.MailSendEvent;
import com.schilings.neiko.autoconfigure.mail.model.MailDetails;
import com.schilings.neiko.autoconfigure.mail.model.MailSendInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import java.io.File;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
public class NeikoMailSender implements MailSender {

	private final JavaMailSender mailSender;

	private final ApplicationEventPublisher publisher;

	/**
	 * 配置文件中我的qq邮箱
	 */
	@Value("${spring.mail.properties.from}")
	private String defaultFrom;

	@Override
	public MailSendInfo sendMail(MailDetails mailDetails) {
		MailSendInfo mailSendInfo = new MailSendInfo(mailDetails);
		// 发送时间
		mailSendInfo.setSentDate(LocalDateTime.now());

		try {
			// 1.检测邮件
			checkMailDetails(mailDetails);
			// 2.发送邮件
			doSendMail(mailDetails);
			// 发送成功
			mailSendInfo.setSuccess(true);

		}
		catch (Exception e) {
			// 发送失败
			mailSendInfo.setSuccess(false);
			mailSendInfo.setErrorMsg(e.getMessage());
			log.error("发送邮件失败: [{}]", mailDetails, e);
		}
		finally {
			// 发布事件
			publisher.publishEvent(new MailSendEvent(mailSendInfo));

		}

		return mailSendInfo;
	}

	private void doSendMail(MailDetails mailDetails) throws MessagingException {
		// true表示支持复杂类型
		MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);
		String from = StringUtils.hasText(mailDetails.getFrom()) ? mailDetails.getFrom() : defaultFrom;
		messageHelper.setFrom(from);
		messageHelper.setSubject(mailDetails.getSubject());
		if (mailDetails.getTo() != null && mailDetails.getTo().length > 0) {
			messageHelper.setTo(mailDetails.getTo());
		}
		if (mailDetails.getCc() != null && mailDetails.getCc().length > 0) {
			messageHelper.setCc(mailDetails.getCc());
		}
		if (mailDetails.getBcc() != null && mailDetails.getBcc().length > 0) {
			messageHelper.setBcc(mailDetails.getBcc());
		}
		// 是否展示html
		boolean showHtml = mailDetails.getShowHtml() != null && mailDetails.getShowHtml();
		messageHelper.setText(mailDetails.getContent(), showHtml);
		if (mailDetails.getFiles() != null) {
			for (File file : mailDetails.getFiles()) {
				messageHelper.addAttachment(file.getName(), file);
			}
		}

		mailSender.send(messageHelper.getMimeMessage());
		log.info("发送邮件成功：[{}]", mailDetails);
	}

}

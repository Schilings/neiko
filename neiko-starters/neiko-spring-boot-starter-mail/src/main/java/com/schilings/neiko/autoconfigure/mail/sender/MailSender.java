package com.schilings.neiko.autoconfigure.mail.sender;

import com.schilings.neiko.autoconfigure.mail.model.MailDetails;
import com.schilings.neiko.autoconfigure.mail.model.MailSendInfo;
import org.springframework.mail.MailSendException;
import org.springframework.util.StringUtils;

import java.util.List;

public interface MailSender {

	/**
	 * 发送邮件
	 * @param mailDetails
	 * @return
	 */
	MailSendInfo sendMail(MailDetails mailDetails);

	/**
	 * 发送邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param showHtml 是否将正文渲染为html
	 * @param to 收件人，多个邮箱使用,号间隔
	 * @return MailSendInfo
	 */
	default MailSendInfo sendMail(String subject, String content, boolean showHtml, String... to) {
		MailDetails details = new MailDetails();
		details.setSubject(subject);
		details.setContent(content);
		details.setShowHtml(showHtml);
		details.setTo(to);
		return sendMail(details);
	}

	/**
	 * 发送普通文本邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param to 收件人
	 * @return MailSendInfo
	 */
	default MailSendInfo sendTextMail(String subject, String content, String... to) {
		return sendMail(subject, content, false, to);
	}

	/**
	 * 发送普通文本邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param to 收件人
	 * @return MailSendInfo
	 */
	default MailSendInfo sendTextMail(String subject, String content, List<String> to) {
		return sendTextMail(subject, content, to.toArray(new String[0]));
	}

	/**
	 * 发送Html邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param to 收件人
	 * @return MailSendInfo
	 */
	default MailSendInfo sendHtmlMail(String subject, String content, String... to) {
		return sendMail(subject, content, true, to);
	}

	/**
	 * 发送Html邮件
	 * @param subject 主题
	 * @param content 邮件正文
	 * @param to 收件人
	 * @return MailSendInfo 邮件发送结果信息
	 */
	default MailSendInfo sendHtmlMail(String subject, String content, List<String> to) {
		return sendHtmlMail(subject, content, to.toArray(new String[0]));
	}

	/**
	 * 检查邮件是否符合标准
	 * @param mail 邮件信息
	 */
	default void checkMailDetails(MailDetails mail) {
		boolean noTo = mail.getTo() == null || mail.getTo().length <= 0;
		boolean noCc = mail.getCc() == null || mail.getCc().length <= 0;
		boolean noBcc = mail.getBcc() == null || mail.getBcc().length <= 0;
		if (noTo && noCc && noBcc) {
			throw new MailSendException("The email should have at least one recipient");
		}
		if (!StringUtils.hasText(mail.getSubject())) {
			throw new MailSendException("The subject of the email cannot be empty");
		}
		if (!StringUtils.hasText(mail.getContent())) {
			throw new MailSendException("The content of the email cannot be empty");
		}
	}

}

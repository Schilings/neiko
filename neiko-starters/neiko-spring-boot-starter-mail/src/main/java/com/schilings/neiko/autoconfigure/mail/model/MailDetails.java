package com.schilings.neiko.autoconfigure.mail.model;


import lombok.Data;

import java.io.File;

@Data
public class MailDetails {


    /**
     * 发件人
     */
    private String from;

    /**
     * 收件人
     */
    private String[] to;

    /**
     * 邮件主题
     */
    private String subject;

    /**
     * 是否渲染html
     */
    private Boolean showHtml;

    /**
     * 邮件内容
     */
    private String content;

    /**
     * 抄送
     */
    private String[] cc;

    /**
     * 密送
     */
    private String[] bcc;

    /**
     * 附件
     */
    private File[] files;
}

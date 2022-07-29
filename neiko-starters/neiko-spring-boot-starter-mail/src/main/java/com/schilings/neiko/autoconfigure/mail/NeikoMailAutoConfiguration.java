package com.schilings.neiko.autoconfigure.mail;


import com.schilings.neiko.autoconfigure.mail.sender.MailSender;
import com.schilings.neiko.autoconfigure.mail.sender.NeikoMailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

@AutoConfiguration(after = MailSenderAutoConfiguration.class)
@RequiredArgsConstructor
public class NeikoMailAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MailSender.class)
    @ConditionalOnProperty(prefix = "spring.mail", name = "host")
    public MailSender mailSenderImpl(JavaMailSender javaMailSender,
                                     ApplicationEventPublisher applicationEventPublisher) {
        return new NeikoMailSender(javaMailSender, applicationEventPublisher);
    }



}

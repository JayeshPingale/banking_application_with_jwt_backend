package com.testlab.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("jayeshpingale27@gmail.com");
        mailSender.setPassword("mikyvjtnwaudukgy");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
    
    public void sendAccountCreationMail(String toEmail, String userName) throws MessagingException {
    	JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    	MimeMessage mimeMessage = mailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setTo(toEmail);
        helper.setSubject("🎉 Account Created Successfully!");

        String htmlContent = "<html>" +
                "<body style='font-family: Arial, sans-serif;'>" +
                "<h2 style='color: #2E86C1;'>Hello " + userName + ",</h2>" +
                "<p>Your account has been <b style='color:green;'>created successfully</b> 🎉</p>" +
                "<p>Now you can login and enjoy our services.</p>" +
                "<hr>" +
                "<small style='color: gray;'>This is an automated email, please do not reply.</small>" +
                "</body>" +
                "</html>";

        helper.setText(htmlContent, true); // ✅ true = HTML enabled
        mailSender.send(mimeMessage);
    }
}



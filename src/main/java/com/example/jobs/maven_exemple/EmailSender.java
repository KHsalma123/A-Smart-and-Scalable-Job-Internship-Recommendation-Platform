package com.example.jobs.maven_exemple;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void sendEmail(String recipientEmail, String code) {
        final String fromEmail = "email@gmail.com";
        final String password = "password of your email";   

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); 
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Code de réinitialisation");
            message.setText("Voici votre code pour réinitialiser le mot de passe : " + code);

            Transport.send(message);
            System.out.println("Email envoyé à " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

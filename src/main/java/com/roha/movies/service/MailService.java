package com.roha.movies.service;

import java.net.MalformedURLException;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.roha.movies.domain.Movie;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;


@ApplicationScoped
public class MailService {
    private Session session;

    @Inject
    MailConfig mailConfig;

    private void createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", mailConfig.host());
        props.put("mail.smtp.socketFactory.port", mailConfig.port());
        props.put("mail.smtp.socketFactory.class",
            "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.user", mailConfig.username());
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", mailConfig.port());
        //get Session
        session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailConfig.username(), mailConfig.password());
            }
        });
    }

    public void sendNoContentMovie(Movie movie) {
        try {
            send("error loading content for movie", new ObjectMapper().writeValueAsString(movie.asDTO()));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (EmailException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(String subject, String content) throws MalformedURLException, EmailException {

// Create the email message
        HtmlEmail email = new HtmlEmail();
        email.setHostName(mailConfig.host());
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator(mailConfig.username(), mailConfig.password()));
        email.setSSLOnConnect(true);
        email.getMailSession().getProperties().put("mail.smtp.ssl.trust", "smtp.gmail.com");
        email.getMailSession().getProperties().put("mail.smtp.host", "smtp.gmail.com");
        email.getMailSession().getProperties().put("mail.smtp.socketFactory.port", "465");
        email.getMailSession().getProperties().put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        email.getMailSession().getProperties().put("mail.smtp.auth", "true");
        email.getMailSession().getProperties().put("mail.smtp.port", "465");
        email.addTo("ronald.haring@gmail.com", "Ronald to");
        email.setFrom("ronald.haring@gmail.com", "Ronald from");
        email.setSubject(subject);

        String emailContent = "<html>";
        emailContent += "</html>";

        email.setHtmlMsg(emailContent);


// set the html message

// set the alternative message
        email.setTextMsg("Your email client does not support HTML messages");

// send the email
        email.send();
    }

    public String mailConfigDump() {
        String dump =
            "mailConfig.host='" + mailConfig.host() +
                "',mailConfig.port='" + mailConfig.port() +
                "'mailConfig.username='" + mailConfig.username() +
                "'mailConfig.password='" + mailConfig.password();
        return dump;
    }
}

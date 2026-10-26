package com.ecommerce.tienda_kalza.adapter.service;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    private final JavaMailSender emisor;
    private final String remitente;

    public EmailService(JavaMailSender emisor, @Value("${app.email.remitente:}") String remitente) {
        this.emisor = emisor;
        this.remitente = remitente;
    }

    public void enviar(String destinatario, String asunto, String cuerpoHtml) {
        if (remitente == null || remitente.isBlank()) {
            throw new MailPreparationException("Configura el remitente SMTP en las propiedades de correo.");
        }
        try {
            var mensaje = emisor.createMimeMessage();
            var helper = new MimeMessageHelper(mensaje, false, StandardCharsets.UTF_8.name());
            helper.setFrom(remitente);
            helper.setTo(destinatario);
            helper.setSubject(asunto);
            helper.setText(cuerpoHtml, true);
            emisor.send(mensaje);
        } catch (MessagingException e) {
            throw new MailPreparationException("No se pudo preparar el correo.", e);
        }
    }
}

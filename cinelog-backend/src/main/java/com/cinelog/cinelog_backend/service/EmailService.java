package com.cinelog.cinelog_backend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarCorreoActivacion(String to, String nombre, String token) {
        String asunto = "Activa tu cuenta en CineLog";

        String enlaceActivacion = "http://localhost:5173/activar?token=" + token;

        String cuerpo = """
            <html>
                <body style="background-color:#0A1828; color:#FFFFFF; font-family:sans-serif; padding:20px;">
                    <h2 style="color:#BFA181;">¡Bienvenido a CineLog!</h2>
                    <p>Hola <strong>%s</strong>,</p>
                    <p>Gracias por registrarte en <strong>CineLog</strong>.</p>
                    <p>Haz clic en el siguiente botón para activar tu cuenta:</p>
                    <p style="margin: 20px 0;">
                        <a href="%s"
                           style="padding: 12px 20px; background-color: #178582; color: white;
                                  text-decoration: none; border-radius: 6px; font-weight: bold;">
                            Activar cuenta
                        </a>
                    </p>
                    <p>Si tú no hiciste esta solicitud, puedes ignorar este correo.</p>
                    <hr style="border-color: #BFA181; margin-top: 40px;" />
                    <p style="font-size: 12px; color: #BFA181;">CineLog · Proyecto personal de cine</p>
                </body>
            </html>
            """.formatted(nombre, enlaceActivacion);

        enviarCorreoHTML(to, asunto, cuerpo);
    }

    public void enviarCorreoRecuperacion(String to, String nombre, String token) {
        String asunto = "Restablece tu contraseña en CineLog";

        String enlaceRecuperacion = "http://localhost:5173/restablecer?token=" + token;
        System.out.println("🔗 Enlace de recuperación generado: " + enlaceRecuperacion);


        String cuerpo = """
        <html>
            <body style="background-color:#0A1828; color:#FFFFFF; font-family:sans-serif; padding:20px;">
                <h2 style="color:#BFA181;">Restablecer contraseña</h2>
                <p>Hola <strong>%s</strong>,</p>
                <p>Recibimos una solicitud para restablecer tu contraseña.</p>
                <p>Haz clic en el siguiente botón (válido por 30 minutos):</p>
                <p style="margin: 20px 0;">
                    <a href="%s"
                       style="padding: 12px 20px; background-color: #EF4444; color: white;
                              text-decoration: none; border-radius: 6px; font-weight: bold;">
                        Restablecer contraseña
                    </a>
                </p>
                <p>Si tú no hiciste esta solicitud, puedes ignorar este mensaje.</p>
                <hr style="border-color: #BFA181; margin-top: 40px;" />
                <p style="font-size: 12px; color: #BFA181;">CineLog · Proyecto personal de cine</p>
            </body>
        </html>
        """.formatted(nombre, enlaceRecuperacion);

        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(asunto);
            helper.setFrom(from);
            helper.setText(cuerpo, true); // HTML
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el correo de recuperación: " + e.getMessage());
        }
    }


    private void enviarCorreoHTML(String to, String asunto, String cuerpo) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(asunto);
            helper.setFrom(from);
            helper.setText(cuerpo, true); // HTML
            mailSender.send(mensaje);
        } catch (MessagingException e) {
            throw new RuntimeException("No se pudo enviar el correo: " + e.getMessage());
        }
    }
}

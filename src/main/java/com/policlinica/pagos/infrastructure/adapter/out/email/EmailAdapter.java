package com.policlinica.pagos.infrastructure.adapter.out.email;

import com.policlinica.pagos.domain.port.out.EmailPort;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailAdapter implements EmailPort {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Override
    public void enviarEmailConBoleta(String destinatario, String nombre, byte[] pdf, 
                                      String idTransaccion, String detalleCita) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo(destinatario);
            helper.setFrom(fromEmail);
            helper.setSubject("✅ Confirmación de cita médica - Boleta N° " + idTransaccion);
            
            String htmlContent = String.format("""
                <!DOCTYPE html>
                <html>
                <head><meta charset="UTF-8"></head>
                <body style="font-family: Arial, sans-serif;">
                    <h2 style="color: #2c3e50;">¡Hola %s!</h2>
                    <p>Tu pago ha sido <strong style="color: green;">procesado exitosamente</strong>.</p>
                    <p><strong>Detalle de la cita:</strong><br>%s</p>
                    <p><strong>ID de transacción:</strong> %s</p>
                    <p>Adjunto encontrarás la boleta de tu cita médica.</p>
                    <hr>
                    <p style="color: #7f8c8d; font-size: 12px;">
                        Este es un correo automático, por favor no responder.<br>
                        Policlínica - Tu salud es nuestra prioridad.
                    </p>
                </body>
                </html>
                """, nombre, detalleCita, idTransaccion);
            
            helper.setText(htmlContent, true);
            helper.addAttachment("boleta_" + idTransaccion + ".pdf", 
                    new org.springframework.core.io.ByteArrayResource(pdf));
            
            mailSender.send(message);
            
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el email", e);
        }
    }
}
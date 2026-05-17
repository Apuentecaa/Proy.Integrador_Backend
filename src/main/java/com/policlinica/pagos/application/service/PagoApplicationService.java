package com.policlinica.pagos.application.service;

import com.policlinica.pagos.domain.model.Notificacion;
import com.policlinica.pagos.domain.model.Pago;
import com.policlinica.pagos.domain.port.in.ProcesarPagoUseCase;
import com.policlinica.pagos.domain.port.out.*;
import com.policlinica.pagos.domain.service.PagoDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
public class PagoApplicationService implements ProcesarPagoUseCase {
    
    private final PagoRepositoryPort pagoRepository;
    private final NotificacionRepositoryPort notificacionRepository;
    private final PagoDomainService pagoDomainService;
    private final PdfGeneratorPort pdfGenerator;
    private final EmailPort emailPort;
    
    public PagoApplicationService(
            PagoRepositoryPort pagoRepository,
            NotificacionRepositoryPort notificacionRepository,
            PagoDomainService pagoDomainService,
            PdfGeneratorPort pdfGenerator,
            EmailPort emailPort) {
        this.pagoRepository = pagoRepository;
        this.notificacionRepository = notificacionRepository;
        this.pagoDomainService = pagoDomainService;
        this.pdfGenerator = pdfGenerator;
        this.emailPort = emailPort;
    }
    
    @Override
    @Transactional
    public Pago procesarPago(ProcesarPagoCommand command) {
        
        pagoDomainService.verificarPagoUnicoPorCita(command.citaId());
        
        boolean esTarjeta = "TARJETA_CREDITO".equals(command.metodo()) || 
                            "TARJETA_DEBITO".equals(command.metodo());
        
        if (esTarjeta) {
            boolean tarjetaValida = pagoDomainService.validarTarjeta(
                command.numeroTarjeta(),
                command.cvv(),
                command.fechaExpiracion()
            );
            
            if (!tarjetaValida) {
                Pago pagoRechazado = new Pago(
                    command.citaId(),
                    command.monto(),
                    command.metodo(),
                    command.emailContacto(),
                    command.telefonoContacto()
                );
                pagoRechazado.setMetodoDetalle(command.metodoDetalle());
                pagoRechazado.rechazar("Tarjeta inválida");
                return pagoRepository.guardar(pagoRechazado);
            }
        }
        
        Pago pago = new Pago(
            command.citaId(),
            command.monto(),
            command.metodo(),
            command.emailContacto(),
            command.telefonoContacto()
        );
        pago.setMetodoDetalle(command.metodoDetalle());
        pago.procesando();
        
        String transaccionExterna = "EXT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        pago.setTransaccionExternaId(transaccionExterna);
        pago.setCodigoAutorizacion(String.format("%06d", (int)(Math.random() * 1000000)));
        
        if (esTarjeta) {
            pago.setUltimosDigitosTarjeta(
                pagoDomainService.obtenerUltimosDigitos(command.numeroTarjeta())
            );
        }
        
        pago.completar();
        
        Pago pagoGuardado = pagoRepository.guardar(pago);
        
        String detalleCita = String.format("Cita #%d - Monto: S/ %.2f", command.citaId(), command.monto());
        String nombrePaciente = command.nombreTitular();
        String dniPaciente = "XXXXXXXX";
        
        // Generar PDF con try-catch para que no bloquee
        byte[] pdf = null;
        try {
            pdf = pdfGenerator.generarBoleta(pagoGuardado, detalleCita, nombrePaciente, dniPaciente);
            pdfGenerator.guardarPDF(pdf, "PAGO-" + pagoGuardado.getId());
        } catch (Exception e) {
            System.err.println("❌ Error generando PDF: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Email con try-catch para que NO BLOQUEE el pago
        try {
            if (pdf != null) {
                emailPort.enviarEmailConBoleta(
                    pagoGuardado.getEmailContacto(),
                    nombrePaciente,
                    pdf,
                    "PAGO-" + pagoGuardado.getId(),
                    detalleCita
                );
                
                Notificacion notificacion = new Notificacion(
                    pagoGuardado.getId(),
                    "EMAIL",
                    pagoGuardado.getEmailContacto(),
                    "Boleta de pago enviada exitosamente"
                );
                notificacion.marcarComoEnviado();
                notificacionRepository.guardar(notificacion);
            } else {
                throw new Exception("PDF no generado correctamente");
            }
        } catch (Exception e) {
            System.err.println("❌ Error enviando email: " + e.getMessage());
            e.printStackTrace();
            // Registrar notificación de error
            try {
                Notificacion notificacion = new Notificacion(
                    pagoGuardado.getId(),
                    "EMAIL",
                    pagoGuardado.getEmailContacto(),
                    "Error al enviar email: " + e.getMessage()
                );
                notificacion.marcarComoFallido(e.getMessage());
                notificacionRepository.guardar(notificacion);
            } catch (Exception ex) {
                System.err.println("❌ Error guardando notificación: " + ex.getMessage());
            }
        }
        
        return pagoGuardado;
    }
}
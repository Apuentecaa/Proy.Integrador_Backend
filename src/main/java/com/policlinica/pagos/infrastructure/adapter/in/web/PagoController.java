package com.policlinica.pagos.infrastructure.adapter.in.web;

import com.policlinica.pagos.domain.model.Pago;
import com.policlinica.pagos.domain.port.in.ConsultarPagoUseCase;
import com.policlinica.pagos.domain.port.in.ProcesarPagoUseCase;
import com.policlinica.pagos.infrastructure.adapter.in.web.dto.PagoRequest;
import com.policlinica.pagos.infrastructure.adapter.in.web.dto.PagoResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {
    
    private final ProcesarPagoUseCase procesarPagoUseCase;
    private final ConsultarPagoUseCase consultarPagoUseCase;
    
    public PagoController(ProcesarPagoUseCase procesarPagoUseCase, 
                          ConsultarPagoUseCase consultarPagoUseCase) {
        this.procesarPagoUseCase = procesarPagoUseCase;
        this.consultarPagoUseCase = consultarPagoUseCase;
    }
    
    @PostMapping("/procesar")
    public ResponseEntity<PagoResponse> procesarPago(@RequestBody PagoRequest request) {
        try {
            ProcesarPagoUseCase.ProcesarPagoCommand command = 
                new ProcesarPagoUseCase.ProcesarPagoCommand(
                    request.getCitaId(),
                    request.getMonto(),
                    request.getMetodo(),
                    request.getMetodoDetalle(),
                    request.getEmailContacto(),
                    request.getTelefonoContacto(),
                    request.getNumeroTarjeta(),
                    request.getCvv(),
                    request.getFechaExpiracion(),
                    request.getNombreTitular()
                );
            
            Pago pago = procesarPagoUseCase.procesarPago(command);
            
            PagoResponse response = new PagoResponse();
            response.setExitoso("COMPLETADO".equals(pago.getEstado()));
            response.setMensaje(response.isExitoso() ? 
                "Pago procesado exitosamente. Se ha enviado la boleta a tu correo." : 
                "El pago fue rechazado. Verifica los datos de tu tarjeta.");
            response.setIdTransaccion(pago.getId());
            response.setEstado(pago.getEstado());
            response.setPdfUrl("http://localhost:8080/api/pagos/boleta/PAGO-" + pago.getId());
            response.setCodigoAutorizacion(pago.getCodigoAutorizacion());
            response.setUltimosDigitos(pago.getUltimosDigitosTarjeta());
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalStateException e) {
            PagoResponse errorResponse = new PagoResponse(false, e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        } catch (Exception e) {
            PagoResponse errorResponse = new PagoResponse(false, "Error interno: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    @GetMapping("/estado/{id}")
    public ResponseEntity<PagoResponse> consultarEstado(@PathVariable Long id) {
        Optional<Pago> pagoOpt = consultarPagoUseCase.consultarPorId(id);
        
        if (pagoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Pago pago = pagoOpt.get();
        PagoResponse response = new PagoResponse();
        response.setExitoso(true);
        response.setIdTransaccion(pago.getId());
        response.setEstado(pago.getEstado());
        response.setMensaje("Transacción encontrada");
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/boleta/{idTransaccion}")
    public ResponseEntity<byte[]> descargarBoleta(@PathVariable String idTransaccion) {
        try {
            Path path = Paths.get("./boletas/boleta_" + idTransaccion + ".pdf");
            byte[] pdf = Files.readAllBytes(path);
            
            return ResponseEntity.ok()
                    .header("Content-Type", "application/pdf")
                    .header("Content-Disposition", "inline; filename=" + idTransaccion + ".pdf")
                    .body(pdf);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/cita/{citaId}")
    public ResponseEntity<PagoResponse> consultarPorCita(@PathVariable Long citaId) {
        Optional<Pago> pagoOpt = consultarPagoUseCase.consultarPorCitaId(citaId);
        
        if (pagoOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        Pago pago = pagoOpt.get();
        PagoResponse response = new PagoResponse();
        response.setExitoso(true);
        response.setIdTransaccion(pago.getId());
        response.setEstado(pago.getEstado());
        response.setMensaje("Pago encontrado para la cita " + citaId);
        
        return ResponseEntity.ok(response);
    }
}
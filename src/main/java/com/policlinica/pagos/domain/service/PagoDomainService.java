package com.policlinica.pagos.domain.service;

import com.policlinica.pagos.domain.model.Pago;
import com.policlinica.pagos.domain.port.out.PagoRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class PagoDomainService {
    
    private final PagoRepositoryPort pagoRepository;
    
    public PagoDomainService(PagoRepositoryPort pagoRepository) {
        this.pagoRepository = pagoRepository;
    }
    
    public boolean validarTarjeta(String numeroTarjeta, String cvv, String fechaExpiracion) {
        if (numeroTarjeta == null || numeroTarjeta.trim().isEmpty()) return false;
        if (cvv == null || cvv.trim().isEmpty()) return false;
        if (fechaExpiracion == null || fechaExpiracion.trim().isEmpty()) return false;
        
        String tarjetaLimpia = numeroTarjeta.replaceAll("\\s", "");
        if (tarjetaLimpia.length() != 16) return false;
        if (!tarjetaLimpia.matches("\\d+")) return false;
        
        if (cvv.length() != 3) return false;
        if (!cvv.matches("\\d+")) return false;
        
        if (!fechaExpiracion.matches("(0[1-9]|1[0-2])/(\\d{2}|\\d{4})")) return false;
        
        return true;
    }
    
    public void verificarPagoUnicoPorCita(Long citaId) {
        if (pagoRepository.existePorCitaId(citaId)) {
            throw new IllegalStateException("La cita ya tiene un pago registrado");
        }
    }
    
    public double calcularComision(double monto, String metodo) {
        if ("TARJETA_CREDITO".equals(metodo)) {
            return monto * 0.035;
        } else if ("TARJETA_DEBITO".equals(metodo)) {
            return monto * 0.02;
        }
        return 0;
    }
    
    public String obtenerUltimosDigitos(String numeroTarjeta) {
        if (numeroTarjeta == null) return null;
        String limpio = numeroTarjeta.replaceAll("\\s", "");
        if (limpio.length() >= 4) {
            return limpio.substring(limpio.length() - 4);
        }
        return null;
    }
}

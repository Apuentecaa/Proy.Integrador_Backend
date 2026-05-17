package com.policlinica.pagos.domain.port.in;

import com.policlinica.pagos.domain.model.Pago;

public interface ProcesarPagoUseCase {
    Pago procesarPago(ProcesarPagoCommand command);
    
    record ProcesarPagoCommand(
        Long citaId,
        Double monto,
        String metodo,
        String metodoDetalle,
        String emailContacto,
        String telefonoContacto,
        String numeroTarjeta,
        String cvv,
        String fechaExpiracion,
        String nombreTitular
    ) {}
}

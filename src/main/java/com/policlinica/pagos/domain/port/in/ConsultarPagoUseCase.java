package com.policlinica.pagos.domain.port.in;

import com.policlinica.pagos.domain.model.Pago;
import java.util.Optional;

public interface ConsultarPagoUseCase {
    Optional<Pago> consultarPorId(Long id);
    Optional<Pago> consultarPorCitaId(Long citaId);
    Optional<Pago> consultarPorTransaccionExterna(String transaccionExternaId);
}
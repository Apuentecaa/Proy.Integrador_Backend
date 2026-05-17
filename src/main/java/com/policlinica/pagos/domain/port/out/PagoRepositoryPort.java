package com.policlinica.pagos.domain.port.out;

import com.policlinica.pagos.domain.model.Pago;
import java.util.Optional;

public interface PagoRepositoryPort {
    Pago guardar(Pago pago);
    Optional<Pago> buscarPorId(Long id);
    Optional<Pago> buscarPorCitaId(Long citaId);
    Optional<Pago> buscarPorTransaccionExterna(String transaccionExternaId);
    boolean existePorCitaId(Long citaId);
}

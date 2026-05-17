package com.policlinica.pagos.application.service;

import com.policlinica.pagos.domain.model.Pago;
import com.policlinica.pagos.domain.port.in.ConsultarPagoUseCase;
import com.policlinica.pagos.domain.port.out.PagoRepositoryPort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class ConsultarPagoApplicationService implements ConsultarPagoUseCase {
    
    private final PagoRepositoryPort pagoRepository;
    
    public ConsultarPagoApplicationService(PagoRepositoryPort pagoRepository) {
        this.pagoRepository = pagoRepository;
    }
    
    @Override
    public Optional<Pago> consultarPorId(Long id) {
        return pagoRepository.buscarPorId(id);
    }
    
    @Override
    public Optional<Pago> consultarPorCitaId(Long citaId) {
        return pagoRepository.buscarPorCitaId(citaId);
    }
    
    @Override
    public Optional<Pago> consultarPorTransaccionExterna(String transaccionExternaId) {
        return pagoRepository.buscarPorTransaccionExterna(transaccionExternaId);
    }
}
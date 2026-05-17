package com.policlinica.pagos.infrastructure.adapter.out.persistence;

import com.policlinica.pagos.domain.model.Pago;
import com.policlinica.pagos.domain.port.out.PagoRepositoryPort;
import com.policlinica.pagos.infrastructure.adapter.out.persistence.entity.PagoEntity;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class PagoRepositoryAdapter implements PagoRepositoryPort {
    
    private final PagoJpaRepository jpaRepository;
    
    public PagoRepositoryAdapter(PagoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Pago guardar(Pago pago) {
        PagoEntity entity = toEntity(pago);
        PagoEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }
    
    @Override
    public Optional<Pago> buscarPorId(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }
    
    @Override
    public Optional<Pago> buscarPorCitaId(Long citaId) {
        return jpaRepository.findByCitaId(citaId).map(this::toDomain);
    }
    
    @Override
    public Optional<Pago> buscarPorTransaccionExterna(String transaccionExternaId) {
        return jpaRepository.findByTransaccionExternaId(transaccionExternaId).map(this::toDomain);
    }
    
    @Override
    public boolean existePorCitaId(Long citaId) {
        return jpaRepository.existsByCitaId(citaId);
    }
    
    private PagoEntity toEntity(Pago pago) {
        PagoEntity entity = new PagoEntity();
        entity.setId(pago.getId());
        entity.setCitaId(pago.getCitaId());
        entity.setMonto(pago.getMonto());
        entity.setMetodo(pago.getMetodo());
        entity.setMetodoDetalle(pago.getMetodoDetalle());
        entity.setEstado(pago.getEstado());
        entity.setFecha(pago.getFecha());
        entity.setEmailContacto(pago.getEmailContacto());
        entity.setTelefonoContacto(pago.getTelefonoContacto());
        entity.setTransaccionExternaId(pago.getTransaccionExternaId());
        entity.setCodigoAutorizacion(pago.getCodigoAutorizacion());
        entity.setUltimosDigitosTarjeta(pago.getUltimosDigitosTarjeta());
        entity.setMoneda(pago.getMoneda());
        entity.setDescuento(pago.getDescuento());
        return entity;
    }
    
    private Pago toDomain(PagoEntity entity) {
        Pago pago = new Pago();
        pago.setId(entity.getId());
        pago.setCitaId(entity.getCitaId());
        pago.setMonto(entity.getMonto());
        pago.setMetodo(entity.getMetodo());
        pago.setMetodoDetalle(entity.getMetodoDetalle());
        pago.setEstado(entity.getEstado());
        pago.setFecha(entity.getFecha());
        pago.setEmailContacto(entity.getEmailContacto());
        pago.setTelefonoContacto(entity.getTelefonoContacto());
        pago.setTransaccionExternaId(entity.getTransaccionExternaId());
        pago.setCodigoAutorizacion(entity.getCodigoAutorizacion());
        pago.setUltimosDigitosTarjeta(entity.getUltimosDigitosTarjeta());
        pago.setMoneda(entity.getMoneda());
        pago.setDescuento(entity.getDescuento());
        return pago;
    }
}
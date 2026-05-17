package com.policlinica.pagos.infrastructure.adapter.out.persistence;

import com.policlinica.pagos.domain.model.Notificacion;
import com.policlinica.pagos.domain.port.out.NotificacionRepositoryPort;
import com.policlinica.pagos.infrastructure.adapter.out.persistence.entity.NotificacionEntity;
import org.springframework.stereotype.Component;

@Component
public class NotificacionRepositoryAdapter implements NotificacionRepositoryPort {
    
    private final NotificacionJpaRepository jpaRepository;
    
    public NotificacionRepositoryAdapter(NotificacionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }
    
    @Override
    public Notificacion guardar(Notificacion notificacion) {
        NotificacionEntity entity = toEntity(notificacion);
        NotificacionEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }
    
    private NotificacionEntity toEntity(Notificacion notificacion) {
        NotificacionEntity entity = new NotificacionEntity();
        entity.setId(notificacion.getId());
        entity.setPagoId(notificacion.getPagoId());
        entity.setTipo(notificacion.getTipo());
        entity.setDestinatario(notificacion.getDestinatario());
        entity.setEstado(notificacion.getEstado());
        entity.setMensaje(notificacion.getMensaje());
        entity.setError(notificacion.getError());
        entity.setFechaEnvio(notificacion.getFechaEnvio());
        entity.setFechaCreacion(notificacion.getFechaCreacion());
        return entity;
    }
    
    private Notificacion toDomain(NotificacionEntity entity) {
        Notificacion notificacion = new Notificacion();
        notificacion.setId(entity.getId());
        notificacion.setPagoId(entity.getPagoId());
        notificacion.setTipo(entity.getTipo());
        notificacion.setDestinatario(entity.getDestinatario());
        notificacion.setEstado(entity.getEstado());
        notificacion.setMensaje(entity.getMensaje());
        notificacion.setError(entity.getError());
        notificacion.setFechaEnvio(entity.getFechaEnvio());
        notificacion.setFechaCreacion(entity.getFechaCreacion());
        return notificacion;
    }
}
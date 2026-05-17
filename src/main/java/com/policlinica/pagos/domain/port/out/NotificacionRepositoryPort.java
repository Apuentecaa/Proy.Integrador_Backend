package com.policlinica.pagos.domain.port.out;

import com.policlinica.pagos.domain.model.Notificacion;

public interface NotificacionRepositoryPort {
    Notificacion guardar(Notificacion notificacion);
}
package com.policlinica.pagos.infrastructure.adapter.out.persistence;

import com.policlinica.pagos.infrastructure.adapter.out.persistence.entity.NotificacionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificacionJpaRepository extends JpaRepository<NotificacionEntity, Long> {
}
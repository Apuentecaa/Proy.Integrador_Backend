package com.policlinica.pagos.infrastructure.adapter.out.persistence;

import com.policlinica.pagos.infrastructure.adapter.out.persistence.entity.PagoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PagoJpaRepository extends JpaRepository<PagoEntity, Long> {
    Optional<PagoEntity> findByCitaId(Long citaId);
    Optional<PagoEntity> findByTransaccionExternaId(String transaccionExternaId);
    boolean existsByCitaId(Long citaId);
}
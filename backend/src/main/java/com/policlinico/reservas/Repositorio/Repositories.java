package com.policlinico.reservas.Repositorio;

import com.policlinico.reservas.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

// ─────────────────────────────────────────────
//  Especialidad
// ─────────────────────────────────────────────
@Repositorio
interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {}

// ─────────────────────────────────────────────
//  Medico
// ─────────────────────────────────────────────
@Repositorio
interface MedicoRepository extends JpaRepository<Medico, Integer> {

    List<Medico> findByEspecialidadId(Integer especialidadId);

    List<Medico> findBySede(String sede);
}

// ─────────────────────────────────────────────
//  Paciente
// ─────────────────────────────────────────────
@Repositorio
interface PacienteRepository extends JpaRepository<Paciente, Integer> {

    Optional<Paciente> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByDni(String dni);
}

// ─────────────────────────────────────────────
//  HorarioMedico
// ─────────────────────────────────────────────
@Repositorio
interface HorarioMedicoRepository extends JpaRepository<HorarioMedico, Integer> {

    /** Todos los bloques de un médico en una fecha, ordenados por hora */
    List<HorarioMedico> findByMedicoIdAndFechaOrderByHoraInicio(
            Integer medicoId, LocalDate fecha);

    /** Solo bloques disponibles de un médico en una fecha */
    List<HorarioMedico> findByMedicoIdAndFechaAndDisponibleOrderByHoraInicio(
            Integer medicoId, LocalDate fecha, Boolean disponible);

    /** Agenda semanal de un médico (lunes → domingo) */
    @Query("""
        SELECT h FROM HorarioMedico h
        WHERE h.medico.id = :medicoId
          AND h.fecha BETWEEN :desde AND :hasta
        ORDER BY h.fecha, h.horaInicio
        """)
    List<HorarioMedico> findSemanaByMedico(
            @Param("medicoId") Integer medicoId,
            @Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    /** Todos los médicos disponibles de una especialidad en una fecha */
    @Query("""
        SELECT h FROM HorarioMedico h
        WHERE h.medico.especialidad.id = :espId
          AND h.fecha = :fecha
          AND h.disponible = true
        ORDER BY h.medico.nombre, h.horaInicio
        """)
    List<HorarioMedico> findDisponiblesByEspecialidadAndFecha(
            @Param("espId") Integer espId,
            @Param("fecha") LocalDate fecha);

    /** Detecta solapamiento antes de crear un nuevo bloque */
    @Query("""
        SELECT COUNT(h) > 0 FROM HorarioMedico h
        WHERE h.medico.id = :medicoId
          AND h.fecha = :fecha
          AND h.horaInicio < :horaFin
          AND h.horaFin > :horaInicio
        """)
    boolean existsSolapamiento(
            @Param("medicoId") Integer medicoId,
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin);
}

// ─────────────────────────────────────────────
//  Cita
// ─────────────────────────────────────────────
@Repositorio
interface CitaRepository extends JpaRepository<Cita, Integer> {

    /** Todas las citas de un paciente, las más recientes primero */
    List<Cita> findByPacienteIdOrderByFechaDescHoraDesc(Integer pacienteId);

    /** Citas de un médico en un día concreto */
    List<Cita> findByMedicoIdAndFechaOrderByHora(Integer medicoId, LocalDate fecha);

    /** Próximas citas del paciente (hoy en adelante, sin canceladas) */
    @Query("""
        SELECT c FROM Cita c
        WHERE c.paciente.id = :pacienteId
          AND c.fecha >= :hoy
          AND c.estado <> 'CANCELADO'
        ORDER BY c.fecha, c.hora
        """)
    List<Cita> findProximasByPaciente(
            @Param("pacienteId") Integer pacienteId,
            @Param("hoy") LocalDate hoy);

    /** Historial de citas pasadas del paciente */
    @Query("""
        SELECT c FROM Cita c
        WHERE c.paciente.id = :pacienteId
          AND c.fecha < :hoy
        ORDER BY c.fecha DESC, c.hora DESC
        """)
    List<Cita> findHistorialByPaciente(
            @Param("pacienteId") Integer pacienteId,
            @Param("hoy") LocalDate hoy);

    /** Comprueba si el slot médico+fecha+hora ya está ocupado */
    @Query("""
        SELECT COUNT(c) > 0 FROM Cita c
        WHERE c.medico.id = :medicoId
          AND c.fecha = :fecha
          AND c.hora = :hora
          AND c.estado <> 'CANCELADO'
        """)
    boolean existsSlotOcupado(
            @Param("medicoId") Integer medicoId,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora);

    /** Horas ya reservadas de un médico en una fecha (para pintar la grilla) */
    @Query("""
        SELECT c.hora FROM Cita c
        WHERE c.medico.id = :medicoId
          AND c.fecha = :fecha
          AND c.estado <> 'CANCELADO'
        """)
    List<LocalTime> findHorasOcupadasByMedicoAndFecha(
            @Param("medicoId") Integer medicoId,
            @Param("fecha") LocalDate fecha);
}
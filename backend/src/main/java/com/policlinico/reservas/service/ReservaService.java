package com.policlinico.reservas.service;

import com.policlinico.reservas.dto.*;
import com.policlinico.reservas.entity.Cita;
import com.policlinico.reservas.exception.NotFoundException;
import com.policlinico.reservas.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final CitaRepository          citaRepo;
    private final PacienteRepository      pacienteRepo;
    private final MedicoRepository        medicoRepo;
    private final HorarioMedicoRepository horarioRepo;

    // ── CREAR RESERVA ────────────────────────────────────────
    @Transactional
    public CitaResponse crear(ReservaRequest req) {
        // 1. El médico debe tener un bloque activo que contenga la hora pedida
        var bloques = horarioRepo
                .findByMedicoIdAndFechaAndDisponibleOrderByHoraInicio(
                        req.getMedicoId(), req.getFecha(), true);

        boolean horaValida = bloques.stream().anyMatch(b ->
                !req.getHora().isBefore(b.getHoraInicio()) &&
                        req.getHora().plusMinutes(30).compareTo(b.getHoraFin()) <= 0);

        if (!horaValida)
            throw new IllegalStateException(
                    "El médico no tiene disponibilidad para la hora solicitada: " + req.getHora());

        // 2. El slot no puede estar ya reservado
        if (citaRepo.existsSlotOcupado(req.getMedicoId(), req.getFecha(), req.getHora()))
            throw new IllegalStateException(
                    "El horario " + req.getHora() + " del " + req.getFecha() +
                            " ya está reservado. Por favor elige otra hora.");

        var paciente = pacienteRepo.findById(req.getPacienteId())
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado: " + req.getPacienteId()));
        var medico   = medicoRepo.findById(req.getMedicoId())
                .orElseThrow(() -> new NotFoundException("Médico no encontrado: " + req.getMedicoId()));

        var cita = Cita.builder()
                .paciente(paciente)
                .medico(medico)
                .fecha(req.getFecha())
                .hora(req.getHora())
                .estado("RESERVADO")
                .build();

        return toDto(citaRepo.save(cita));
    }

    // ── LISTAR TODAS (admin) ─────────────────────────────────
    public List<CitaResponse> listarTodas() {
        return citaRepo.findAll().stream().map(this::toDto).toList();
    }

    // ── LISTAR POR PACIENTE ──────────────────────────────────
    public List<CitaResponse> listarPorPaciente(Integer pacienteId) {
        pacienteRepo.findById(pacienteId)
                .orElseThrow(() -> new NotFoundException("Paciente no encontrado: " + pacienteId));

        return citaRepo.findByPacienteIdOrderByFechaDescHoraDesc(pacienteId)
                .stream().map(this::toDto).toList();
    }

    // ── PRÓXIMAS DEL PACIENTE ────────────────────────────────
    public List<CitaResponse> proximas(Integer pacienteId) {
        return citaRepo.findProximasByPaciente(pacienteId, LocalDate.now())
                .stream().map(this::toDto).toList();
    }

    // ── HISTORIAL DEL PACIENTE ───────────────────────────────
    public List<CitaResponse> historial(Integer pacienteId) {
        return citaRepo.findHistorialByPaciente(pacienteId, LocalDate.now())
                .stream().map(this::toDto).toList();
    }

    // ── AGENDA DE UN MÉDICO ──────────────────────────────────
    public List<CitaResponse> agendaMedico(Integer medicoId, LocalDate fecha) {
        medicoRepo.findById(medicoId)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado: " + medicoId));

        return citaRepo.findByMedicoIdAndFechaOrderByHora(medicoId, fecha)
                .stream().map(this::toDto).toList();
    }

    // ── OBTENER POR ID ───────────────────────────────────────
    public CitaResponse obtenerPorId(Integer id) {
        return toDto(citaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada: " + id)));
    }

    // ── ACTUALIZAR (reasignar médico / fecha / hora) ─────────
    @Transactional
    public CitaResponse actualizar(Integer id, ReservaUpdateRequest req) {
        var cita = citaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada: " + id));

        if ("ATENDIDO".equals(cita.getEstado()))
            throw new IllegalStateException("No se puede modificar una cita ya atendida");
        if ("CANCELADO".equals(cita.getEstado()))
            throw new IllegalStateException("No se puede modificar una cita cancelada");

        // Validar disponibilidad en el nuevo slot
        var bloques = horarioRepo
                .findByMedicoIdAndFechaAndDisponibleOrderByHoraInicio(
                        req.getMedicoId(), req.getFecha(), true);

        boolean horaValida = bloques.stream().anyMatch(b ->
                !req.getHora().isBefore(b.getHoraInicio()) &&
                        req.getHora().plusMinutes(30).compareTo(b.getHoraFin()) <= 0);

        if (!horaValida)
            throw new IllegalStateException(
                    "El médico no tiene disponibilidad para la hora solicitada: " + req.getHora());

        // Verificar que el nuevo slot no esté ocupado por otra cita
        boolean slotOcupado = citaRepo.existsSlotOcupado(
                req.getMedicoId(), req.getFecha(), req.getHora());

        // Excluir la misma cita si la hora/fecha/médico no cambió
        boolean esMismoCita = cita.getMedico().getId().equals(req.getMedicoId())
                && cita.getFecha().equals(req.getFecha())
                && cita.getHora().equals(req.getHora());

        if (slotOcupado && !esMismoCita)
            throw new IllegalStateException(
                    "El horario " + req.getHora() + " del " + req.getFecha() + " ya está ocupado");

        var medico = medicoRepo.findById(req.getMedicoId())
                .orElseThrow(() -> new NotFoundException("Médico no encontrado: " + req.getMedicoId()));

        cita.setMedico(medico);
        cita.setFecha(req.getFecha());
        cita.setHora(req.getHora());

        return toDto(citaRepo.save(cita));
    }

    // ── CANCELAR ─────────────────────────────────────────────
    @Transactional
    public CitaResponse cancelar(Integer id) {
        var cita = citaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada: " + id));

        if ("CANCELADO".equals(cita.getEstado()))
            throw new IllegalStateException("La cita ya está cancelada");
        if ("ATENDIDO".equals(cita.getEstado()))
            throw new IllegalStateException("No se puede cancelar una cita ya atendida");

        cita.setEstado("CANCELADO");
        return toDto(citaRepo.save(cita));
    }

    // ── MARCAR ATENDIDA ──────────────────────────────────────
    @Transactional
    public CitaResponse atender(Integer id) {
        var cita = citaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada: " + id));

        if (!"RESERVADO".equals(cita.getEstado()))
            throw new IllegalStateException(
                    "Solo se puede atender una cita en estado RESERVADO. Estado actual: " + cita.getEstado());

        cita.setEstado("ATENDIDO");
        return toDto(citaRepo.save(cita));
    }

    // ── ELIMINAR ─────────────────────────────────────────────
    @Transactional
    public void eliminar(Integer id) {
        var cita = citaRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Cita no encontrada: " + id));

        if ("RESERVADO".equals(cita.getEstado()))
            throw new IllegalStateException(
                    "No se puede eliminar una cita activa. Cancélala primero");

        citaRepo.deleteById(id);
    }

    // ── MAPPER ───────────────────────────────────────────────
    private CitaResponse toDto(Cita c) {
        var r = new CitaResponse();
        r.setId(c.getId());
        r.setPacienteId(c.getPaciente().getId());
        r.setPacienteNombre(c.getPaciente().getNombre());
        r.setPacienteDni(c.getPaciente().getDni());
        r.setMedicoId(c.getMedico().getId());
        r.setMedicoNombre(c.getMedico().getNombre());
        r.setEspecialidad(c.getMedico().getEspecialidad().getNombre());
        r.setSede(c.getMedico().getSede());
        r.setFecha(c.getFecha());
        r.setHora(c.getHora());
        r.setEstado(c.getEstado());
        r.setFechaCreacion(c.getFechaCreacion());
        return r;
    }
}
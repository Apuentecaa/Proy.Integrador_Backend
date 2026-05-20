package com.policlinico.reservas.service;

import com.policlinico.reservas.dto.*;
import com.policlinico.reservas.entity.HorarioMedico;
import com.policlinico.reservas.exception.NotFoundException;
import com.policlinico.reservas.repository.HorarioMedicoRepository;
import com.policlinico.reservas.repository.MedicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HorarioService {

    private final HorarioMedicoRepository horarioRepo;
    private final MedicoRepository        medicoRepo;

    // ── CREAR ────────────────────────────────────────────────
    @Transactional
    public HorarioResponse crear(HorarioRequest req) {
        if (!req.getHoraInicio().isBefore(req.getHoraFin()))
            throw new IllegalArgumentException("hora_inicio debe ser anterior a hora_fin");

        var medico = medicoRepo.findById(req.getMedicoId())
                .orElseThrow(() -> new NotFoundException("Médico no encontrado: " + req.getMedicoId()));

        if (horarioRepo.existsSolapamiento(
                medico.getId(), req.getFecha(), req.getHoraInicio(), req.getHoraFin()))
            throw new IllegalStateException(
                    "El médico ya tiene un bloque de horario que se solapa con " +
                            req.getHoraInicio() + " - " + req.getHoraFin());

        var horario = HorarioMedico.builder()
                .medico(medico)
                .fecha(req.getFecha())
                .horaInicio(req.getHoraInicio())
                .horaFin(req.getHoraFin())
                .disponible(req.getDisponible() != null ? req.getDisponible() : true)
                .build();

        return toDto(horarioRepo.save(horario));
    }

    // ── LISTAR POR MÉDICO Y FECHA ────────────────────────────
    public List<HorarioResponse> listarPorMedicoYFecha(Integer medicoId, LocalDate fecha) {
        medicoRepo.findById(medicoId)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado: " + medicoId));

        return horarioRepo
                .findByMedicoIdAndFechaOrderByHoraInicio(medicoId, fecha)
                .stream().map(this::toDto).toList();
    }

    // ── LISTAR SEMANA ────────────────────────────────────────
    public List<HorarioResponse> listarSemana(Integer medicoId, LocalDate lunes) {
        medicoRepo.findById(medicoId)
                .orElseThrow(() -> new NotFoundException("Médico no encontrado: " + medicoId));

        return horarioRepo
                .findSemanaByMedico(medicoId, lunes, lunes.plusDays(6))
                .stream().map(this::toDto).toList();
    }

    // ── OBTENER POR ID ───────────────────────────────────────
    public HorarioResponse obtenerPorId(Integer id) {
        return toDto(horarioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Horario no encontrado: " + id)));
    }

    // ── ACTUALIZAR ───────────────────────────────────────────
    @Transactional
    public HorarioResponse actualizar(Integer id, HorarioRequest req) {
        var horario = horarioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Horario no Actualizado: " + id));

        if (!req.getHoraInicio().isBefore(req.getHoraFin()))
            throw new IllegalArgumentException("hora_inicio debe ser anterior a hora_fin");

        // Validar solapamiento excluyendo el propio registro
        boolean solapa = horarioRepo
                .findByMedicoIdAndFechaOrderByHoraInicio(horario.getMedico().getId(), req.getFecha())
                .stream()
                .filter(h -> !h.getId().equals(id))
                .anyMatch(h -> req.getHoraInicio().isBefore(h.getHoraFin())
                        && req.getHoraFin().isAfter(h.getHoraInicio()));
        if (solapa)
            throw new IllegalStateException("El nuevo rango se solapa con otro horario existente");

        var medico = medicoRepo.findById(req.getMedicoId())
                .orElseThrow(() -> new NotFoundException("Médico inexistente: " + req.getMedicoId()));

        horario.setMedico(medico);
        horario.setFecha(req.getFecha());
        horario.setHoraInicio(req.getHoraInicio());
        horario.setHoraFin(req.getHoraFin());
        if (req.getDisponible() != null) horario.setDisponible(req.getDisponible());

        return toDto(horarioRepo.save(horario));
    }

    // ── CAMBIAR DISPONIBILIDAD ───────────────────────────────
    @Transactional
    public HorarioResponse cambiarDisponibilidad(Integer id, boolean disponible) {
        var horario = horarioRepo.findById(id)
                .orElseThrow(() -> new NotFoundException("Horario no encontrado: " + id));
        horario.setDisponible(disponible);
        return toDto(horarioRepo.save(horario));
    }

    // ── ELIMINAR ─────────────────────────────────────────────
    @Transactional
    public void eliminar(Integer id) {
        if (!horarioRepo.existsById(id))
            throw new NotFoundException("Horario no encontrado: " + id);
        horarioRepo.deleteById(id);
    }

    // ── MAPPER ───────────────────────────────────────────────
    private HorarioResponse toDto(HorarioMedico h) {
        var r = new HorarioResponse();
        r.setId(h.getId());
        r.setMedicoId(h.getMedico().getId());
        r.setMedicoNombre(h.getMedico().getNombre());
        r.setEspecialidad(h.getMedico().getEspecialidad().getNombre());
        r.setSede(h.getMedico().getSede());
        r.setFecha(h.getFecha());
        r.setHoraInicio(h.getHoraInicio());
        r.setHoraFin(h.getHoraFin());
        r.setDisponible(h.getDisponible());
        return r;
    }
}
package com.policlinico.reservas.DTO;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

// ══════════════════════════════════════════════════════
//  AUTH
// ══════════════════════════════════════════════════════

@Data
public class LoginRequest {
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;
}

@Data
public class RegisterRequest {
    @NotBlank(message = "El DNI es obligatorio")
    @Size(max = 15, message = "DNI máximo 15 caracteres")
    private String dni;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank @Email
    private String email;

    @Size(max = 20)
    private String telefono;

    @NotBlank
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;
}

@Data
public class AuthResponse {
    private String token;
    private String tipo = "Bearer";
    private Integer pacienteId;
    private String nombre;
    private String email;

    public AuthResponse(String token, Integer pacienteId, String nombre, String email) {
        this.token = token;
        this.pacienteId = pacienteId;
        this.nombre = nombre;
        this.email = email;
    }
}

// ══════════════════════════════════════════════════════
//  HORARIO
// ══════════════════════════════════════════════════════

@Data
public class HorarioRequest {
    @NotNull(message = "El médico es obligatorio")
    private Integer medicoId;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser pasada")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;

    private Boolean disponible = true;
}

@Data
public class HorarioResponse {
    private Integer id;
    private Integer medicoId;
    private String  medicoNombre;
    private String  especialidad;
    private String  sede;
    private LocalDate fecha;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Boolean disponible;
}

// ══════════════════════════════════════════════════════
//  DISPONIBILIDAD
// ══════════════════════════════════════════════════════

@Data
public class SlotDTO {
    private LocalTime hora;
    private boolean   disponible;

    public SlotDTO(LocalTime hora, boolean disponible) {
        this.hora       = hora;
        this.disponible = disponible;
    }
}

@Data
public class DisponibilidadResponse {
    private Integer       medicoId;
    private String        medicoNombre;
    private String        especialidad;
    private String        sede;
    private LocalDate     fecha;
    private List<SlotDTO> slots;
}

// ══════════════════════════════════════════════════════
//  RESERVA / CITA
// ══════════════════════════════════════════════════════

@Data
public class ReservaRequest {
    @NotNull(message = "El paciente es obligatorio")
    private Integer pacienteId;

    @NotNull(message = "El médico es obligatorio")
    private Integer medicoId;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "No se puede reservar en fechas pasadas")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;
}

@Data
public class ReservaUpdateRequest {
    // Permite reasignar médico, fecha y hora de una reserva existente
    @NotNull(message = "El médico es obligatorio")
    private Integer medicoId;

    @NotNull(message = "La fecha es obligatoria")
    @FutureOrPresent(message = "No se puede mover a una fecha pasada")
    private LocalDate fecha;

    @NotNull(message = "La hora es obligatoria")
    private LocalTime hora;
}

@Data
public class CitaResponse {
    private Integer       id;
    private Integer       pacienteId;
    private String        pacienteNombre;
    private String        pacienteDni;
    private Integer       medicoId;
    private String        medicoNombre;
    private String        especialidad;
    private String        sede;
    private LocalDate     fecha;
    private LocalTime     hora;
    private String        estado;
    private LocalDateTime fechaCreacion;
}

// ══════════════════════════════════════════════════════
//  WRAPPER GENÉRICO DE RESPUESTA API
// ══════════════════════════════════════════════════════

@Data
public class ApiResponse<T> {
    private boolean success;
    private String  message;
    private T       data;

    private ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data    = data;
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "OK", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
package com.policlinica.pagos.domain.model;

import java.time.LocalDateTime;

public class Notificacion {
    private Long id;
    private Long pagoId;
    private String tipo;
    private String destinatario;
    private String estado;
    private String mensaje;
    private String error;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaCreacion;
    
    public Notificacion() {}
    
    public Notificacion(Long pagoId, String tipo, String destinatario, String mensaje) {
        this.pagoId = pagoId;
        this.tipo = tipo;
        this.destinatario = destinatario;
        this.mensaje = mensaje;
        this.estado = "PENDIENTE";
        this.fechaCreacion = LocalDateTime.now();
    }
    
    public void marcarComoEnviado() {
        this.estado = "ENVIADO";
        this.fechaEnvio = LocalDateTime.now();
    }
    
    public void marcarComoFallido(String errorMsg) {
        this.estado = "FALLIDO";
        this.error = errorMsg;
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPagoId() { return pagoId; }
    public void setPagoId(Long pagoId) { this.pagoId = pagoId; }
    
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    
    public String getDestinatario() { return destinatario; }
    public void setDestinatario(String destinatario) { this.destinatario = destinatario; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    
    public LocalDateTime getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(LocalDateTime fechaEnvio) { this.fechaEnvio = fechaEnvio; }
    
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}
package com.policlinica.pagos.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagoRequest {
    
    @JsonProperty("cita_id")
    private Long citaId;
    
    private Double monto;
    private String metodo;
    
    @JsonProperty("metodo_detalle")
    private String metodoDetalle;
    
    @JsonProperty("email_contacto")
    private String emailContacto;
    
    @JsonProperty("telefono_contacto")
    private String telefonoContacto;
    
    @JsonProperty("numero_tarjeta")
    private String numeroTarjeta;
    
    private String cvv;
    
    @JsonProperty("fecha_expiracion")
    private String fechaExpiracion;
    
    @JsonProperty("nombre_titular")
    private String nombreTitular;
    
    // Getters y Setters
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
    
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    
    public String getMetodoDetalle() { return metodoDetalle; }
    public void setMetodoDetalle(String metodoDetalle) { this.metodoDetalle = metodoDetalle; }
    
    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }
    
    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }
    
    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }
    
    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }
    
    public String getFechaExpiracion() { return fechaExpiracion; }
    public void setFechaExpiracion(String fechaExpiracion) { this.fechaExpiracion = fechaExpiracion; }
    
    public String getNombreTitular() { return nombreTitular; }
    public void setNombreTitular(String nombreTitular) { this.nombreTitular = nombreTitular; }
}
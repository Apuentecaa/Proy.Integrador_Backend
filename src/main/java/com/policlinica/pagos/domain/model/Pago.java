package com.policlinica.pagos.domain.model;

import java.time.LocalDateTime;

public class Pago {
    private Long id;
    private Long citaId;
    private Double monto;
    private String metodo;
    private String metodoDetalle;
    private String estado;
    private LocalDateTime fecha;
    private String emailContacto;
    private String telefonoContacto;
    private String transaccionExternaId;
    private String codigoAutorizacion;
    private String ultimosDigitosTarjeta;
    private String moneda;
    private Double descuento;
    
    public Pago() {}
    
    public Pago(Long citaId, Double monto, String metodo, String emailContacto, String telefonoContacto) {
        this.citaId = citaId;
        this.monto = monto;
        this.metodo = metodo;
        this.emailContacto = emailContacto;
        this.telefonoContacto = telefonoContacto;
        this.estado = "PENDIENTE";
        this.fecha = LocalDateTime.now();
        this.moneda = "PEN";
        this.descuento = 0.0;
    }
    
    public void completar() {
        if (!"PENDIENTE".equals(this.estado) && !"PROCESANDO".equals(this.estado)) {
            throw new IllegalStateException("Solo se pueden completar pagos en estado PENDIENTE o PROCESANDO");
        }
        this.estado = "COMPLETADO";
    }
    
    public void rechazar(String motivo) {
        this.estado = "RECHAZADO";
    }
    
    public void procesando() {
        this.estado = "PROCESANDO";
    }
    
    public void fallar() {
        this.estado = "FALLIDO";
    }
    
    public void reembolsar() {
        if (!"COMPLETADO".equals(this.estado)) {
            throw new IllegalStateException("Solo se pueden reembolsar pagos completados");
        }
        this.estado = "REEMBOLSADO";
    }
    
    public Double getTotalConDescuento() {
        return monto - (descuento != null ? descuento : 0);
    }
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }
    
    public Double getMonto() { return monto; }
    public void setMonto(Double monto) { this.monto = monto; }
    
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
    
    public String getMetodoDetalle() { return metodoDetalle; }
    public void setMetodoDetalle(String metodoDetalle) { this.metodoDetalle = metodoDetalle; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
    
    public String getEmailContacto() { return emailContacto; }
    public void setEmailContacto(String emailContacto) { this.emailContacto = emailContacto; }
    
    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) { this.telefonoContacto = telefonoContacto; }
    
    public String getTransaccionExternaId() { return transaccionExternaId; }
    public void setTransaccionExternaId(String transaccionExternaId) { this.transaccionExternaId = transaccionExternaId; }
    
    public String getCodigoAutorizacion() { return codigoAutorizacion; }
    public void setCodigoAutorizacion(String codigoAutorizacion) { this.codigoAutorizacion = codigoAutorizacion; }
    
    public String getUltimosDigitosTarjeta() { return ultimosDigitosTarjeta; }
    public void setUltimosDigitosTarjeta(String ultimosDigitosTarjeta) { this.ultimosDigitosTarjeta = ultimosDigitosTarjeta; }
    
    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    
    public Double getDescuento() { return descuento; }
    public void setDescuento(Double descuento) { this.descuento = descuento; }
}
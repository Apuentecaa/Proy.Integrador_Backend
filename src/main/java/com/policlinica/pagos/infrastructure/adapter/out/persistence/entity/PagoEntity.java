package com.policlinica.pagos.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pago")
public class PagoEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "cita_id", nullable = false)
    private Long citaId;
    
    @Column(nullable = false)
    private Double monto;
    
    @Column(length = 50)
    private String metodo;
    
    @Column(name = "metodo_detalle", length = 50)
    private String metodoDetalle;
    
    @Column(length = 20)
    private String estado;
    
    private LocalDateTime fecha;
    
    @Column(name = "email_contacto", length = 100)
    private String emailContacto;
    
    @Column(name = "telefono_contacto", length = 20)
    private String telefonoContacto;
    
    @Column(name = "transaccion_externa_id", length = 100)
    private String transaccionExternaId;
    
    @Column(name = "codigo_autorizacion", length = 50)
    private String codigoAutorizacion;
    
    @Column(name = "ultimos_digitos_tarjeta", length = 4)
    private String ultimosDigitosTarjeta;
    
    @Column(length = 3)
    private String moneda;
    
    private Double descuento;
    
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
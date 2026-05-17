package com.policlinica.pagos.infrastructure.adapter.in.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PagoResponse {
    
    @JsonProperty("exitoso")
    private boolean exitoso;
    
    @JsonProperty("mensaje")
    private String mensaje;
    
    @JsonProperty("id_transaccion")
    private Long idTransaccion;
    
    @JsonProperty("estado")
    private String estado;
    
    @JsonProperty("pdf_url")
    private String pdfUrl;
    
    @JsonProperty("codigo_autorizacion")
    private String codigoAutorizacion;
    
    @JsonProperty("ultimos_digitos")
    private String ultimosDigitos;
    
    public PagoResponse() {}
    
    public PagoResponse(boolean exitoso, String mensaje) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
    }
    
    // Getters y Setters
    public boolean isExitoso() { return exitoso; }
    public void setExitoso(boolean exitoso) { this.exitoso = exitoso; }
    
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    
    public Long getIdTransaccion() { return idTransaccion; }
    public void setIdTransaccion(Long idTransaccion) { this.idTransaccion = idTransaccion; }
    
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    
    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }
    
    public String getCodigoAutorizacion() { return codigoAutorizacion; }
    public void setCodigoAutorizacion(String codigoAutorizacion) { this.codigoAutorizacion = codigoAutorizacion; }
    
    public String getUltimosDigitos() { return ultimosDigitos; }
    public void setUltimosDigitos(String ultimosDigitos) { this.ultimosDigitos = ultimosDigitos; }
}

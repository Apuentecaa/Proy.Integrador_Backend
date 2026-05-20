package com.proyectointegrador.backend.dto;

public class DashboardDTO {

    private Long totalMedicos;
    private Long citasHoy;
    private Long confirmadas;
    private Long pendientes;

    public DashboardDTO() {
    }

    public DashboardDTO(Long totalMedicos, Long citasHoy, Long confirmadas, Long pendientes) {
        this.totalMedicos = totalMedicos;
        this.citasHoy = citasHoy;
        this.confirmadas = confirmadas;
        this.pendientes = pendientes;
    }

    public Long getTotalMedicos() {
        return totalMedicos;
    }

    public void setTotalMedicos(Long totalMedicos) {
        this.totalMedicos = totalMedicos;
    }

    public Long getCitasHoy() {
        return citasHoy;
    }

    public void setCitasHoy(Long citasHoy) {
        this.citasHoy = citasHoy;
    }

    public Long getConfirmadas() {
        return confirmadas;
    }

    public void setConfirmadas(Long confirmadas) {
        this.confirmadas = confirmadas;
    }

    public Long getPendientes() {
        return pendientes;
    }

    public void setPendientes(Long pendientes) {
        this.pendientes = pendientes;
    }
}
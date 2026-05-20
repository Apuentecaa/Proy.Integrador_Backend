package com.proyectointegrador.backend.domain.model;

public class Medico {

    private Long id;
    private String nombre;
    private String especialidad;
    private String sede;
    private Double rating;
    private Integer pacientes;
    private String estado;

    public Medico() {
    }

    public Medico(Long id, String nombre, String especialidad,
            String sede, Double rating,
            Integer pacientes, String estado) {

        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.sede = sede;
        this.rating = rating;
        this.pacientes = pacientes;
        this.estado = estado;
    }

    // getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getPacientes() {
        return pacientes;
    }

    public void setPacientes(Integer pacientes) {
        this.pacientes = pacientes;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}

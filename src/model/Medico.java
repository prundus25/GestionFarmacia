package model;

import java.io.Serializable;

public class Medico implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String apellidos;
    private String especialidad;
    private String numeroColegiado;

    public Medico(int id, String nombre, String apellidos, String especialidad, String numeroColegiado) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.especialidad = especialidad;
        this.numeroColegiado = numeroColegiado;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getNumeroColegiado() { return numeroColegiado; }
    public void setNumeroColegiado(String numeroColegiado) { this.numeroColegiado = numeroColegiado; }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    @Override
    public String toString() {
        return String.format("[%d] Dr/a. %-30s | Especialidad: %-20s | Nº Colegiado: %s",
                id, getNombreCompleto(), especialidad, numeroColegiado);
    }
}

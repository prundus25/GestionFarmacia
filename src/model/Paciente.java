package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String dni;
    private List<String> alergias;
    private List<String> enfermedadesCronicas;

    public Paciente(int id, String nombre, String apellidos, LocalDate fechaNacimiento, String dni) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.alergias = new ArrayList<>();
        this.enfermedadesCronicas = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getDni() { return dni; }
    public void setDni(String dni) { this.dni = dni; }

    public List<String> getAlergias() { return alergias; }
    public void setAlergias(List<String> alergias) { this.alergias = alergias; }

    public List<String> getEnfermedadesCronicas() { return enfermedadesCronicas; }
    public void setEnfermedadesCronicas(List<String> enfermedadesCronicas) { this.enfermedadesCronicas = enfermedadesCronicas; }

    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-30s | DNI: %-9s | Nac.: %s | Alergias: %s | Enf.Crónicas: %s",
                id, getNombreCompleto(), dni, fechaNacimiento,
                alergias.isEmpty() ? "Ninguna" : String.join(", ", alergias),
                enfermedadesCronicas.isEmpty() ? "Ninguna" : String.join(", ", enfermedadesCronicas));
    }
}

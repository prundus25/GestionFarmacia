package model;

import model.enums.EstadoReceta;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class Receta implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Paciente paciente;
    private Medico medico;
    private List<LineaReceta> lineas;
    private LocalDate fecha;
    private EstadoReceta estado;
    private boolean cronica;

    public Receta(int id, Paciente paciente, Medico medico, List<LineaReceta> lineas,
                  LocalDate fecha, boolean cronica) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.lineas = lineas;
        this.fecha = fecha;
        this.estado = EstadoReceta.PENDIENTE;
        this.cronica = cronica;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }

    public List<LineaReceta> getLineas() { return lineas; }
    public void setLineas(List<LineaReceta> lineas) { this.lineas = lineas; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public EstadoReceta getEstado() { return estado; }
    public void setEstado(EstadoReceta estado) { this.estado = estado; }

    public boolean isCronica() { return cronica; }
    public void setCronica(boolean cronica) { this.cronica = cronica; }

    @Override
    public String toString() {
        return String.format("[%d] Paciente: %-25s | Médico: %-25s | Fecha: %s | Estado: %-25s | %s",
                id, paciente.getNombreCompleto(), "Dr/a. " + medico.getNombreCompleto(),
                fecha, estado, cronica ? "[CRÓNICA]" : "");
    }
}

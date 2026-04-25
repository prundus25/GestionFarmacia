package model;

import model.enums.EstadoOrden;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class OrdenReposicion implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private LocalDate fecha;
    private List<LineaOrden> lineas;
    private EstadoOrden estado;

    public OrdenReposicion(int id, LocalDate fecha, List<LineaOrden> lineas) {
        this.id = id;
        this.fecha = fecha;
        this.lineas = lineas;
        this.estado = EstadoOrden.PENDIENTE;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public List<LineaOrden> getLineas() { return lineas; }
    public void setLineas(List<LineaOrden> lineas) { this.lineas = lineas; }

    public EstadoOrden getEstado() { return estado; }
    public void setEstado(EstadoOrden estado) { this.estado = estado; }

    @Override
    public String toString() {
        return String.format("[%d] Fecha: %s | Estado: %-10s | Líneas: %d",
                id, fecha, estado, lineas.size());
    }
}

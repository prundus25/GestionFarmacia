package model;

import model.enums.EstadoOrden;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Representa una orden de reposición de medicamentos.
 * <p>
 * Agrupa un conjunto de {@link LineaOrden líneas} que
 * detallan los medicamentos y las cantidades solicitadas.
 * Al crearse, el estado se fija automáticamente a
 * {@link EstadoOrden#PENDIENTE}. Cuando se aprueba, el
 * stock de cada medicamento se incrementa con la cantidad
 * de su línea correspondiente.
 */
public class OrdenReposicion implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Identificador único de la orden en el sistema. */
    private int id;

    /** Fecha de creación de la orden. */
    private LocalDate fecha;

    /** Líneas de medicamentos incluidas en la orden. */
    private List<LineaOrden> lineas;

    /** Estado actual dentro del ciclo de vida. */
    private EstadoOrden estado;

    /**
     * Crea una nueva {@code OrdenReposicion} con estado
     * inicial {@link EstadoOrden#PENDIENTE}.
     *
     * @param id     identificador único asignado por el sistema
     * @param fecha  fecha de creación de la orden
     * @param lineas líneas de medicamentos a reponer
     */
    public OrdenReposicion(int id, LocalDate fecha,
                           List<LineaOrden> lineas) {
        this.id = id;
        this.fecha = fecha;
        this.lineas = lineas;
        this.estado = EstadoOrden.PENDIENTE;
    }

    /**
     * Devuelve el identificador único de esta orden.
     *
     * @return el identificador único
     */
    public int getId() { return id; }

    /**
     * Establece el identificador único de esta orden.
     *
     * @param id el nuevo identificador
     */
    public void setId(int id) { this.id = id; }

    /**
     * Devuelve la fecha de creación de esta orden.
     *
     * @return la fecha de creación
     */
    public LocalDate getFecha() { return fecha; }

    /**
     * Establece la fecha de creación de esta orden.
     *
     * @param fecha la nueva fecha
     */
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    /**
     * Devuelve las líneas de medicamentos de esta orden.
     *
     * @return lista de líneas de la orden
     */
    public List<LineaOrden> getLineas() { return lineas; }

    /**
     * Establece las líneas de medicamentos de esta orden.
     *
     * @param lineas la nueva lista de líneas
     */
    public void setLineas(List<LineaOrden> lineas) {
        this.lineas = lineas;
    }

    /**
     * Devuelve el estado actual de esta orden.
     *
     * @return el estado dentro del ciclo de vida
     */
    public EstadoOrden getEstado() { return estado; }

    /**
     * Establece el estado de esta orden.
     *
     * @param estado el nuevo estado
     */
    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    /**
     * Devuelve una representación textual de la orden con
     * formato tabular para su visualización en consola.
     *
     * @return cadena con id, fecha, estado y número de
     *         líneas
     */
    @Override
    public String toString() {
        return String.format(
                "[%d] Fecha: %s | Estado: %-10s | Líneas: %d",
                id, fecha, estado, lineas.size());
    }
}

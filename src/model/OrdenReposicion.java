package model;

import model.enums.EstadoOrden;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Representa una orden de reposición de medicamentos.
 * 
 * Agrupa un conjunto de LineaOrden líneas que
 * detallan los medicamentos y las cantidades solicitadas.
 * Al crearse, el estado se fija automáticamente a
 * PENDIENTE. Cuando se aprueba, el stock de cada medicamento
 * se incrementa con la cantidad de su línea correspondiente.
 */
public class OrdenReposicion implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private LocalDate fecha;
    private List<LineaOrden> lineas;
    private EstadoOrden estado;
    private int idRecetaOrigen;

    /**
     * Crea una nueva OrdenReposicion con estado inicial PENDIENTE
     * y sin receta origen (orden manual o de alerta de stock).
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
        this.idRecetaOrigen = 0;
    }

    /**
     * Crea una nueva OrdenReposicion con estado inicial PENDIENTE
     * vinculada a una receta cronica concreta.
     *
     * @param id             identificador único asignado por el sistema
     * @param fecha          fecha de creación de la orden
     * @param lineas         líneas de medicamentos a reponer
     * @param idRecetaOrigen id de la receta cronica que origina la orden;
     *                       usar 0 si no procede de ninguna receta
     */
    public OrdenReposicion(int id, LocalDate fecha,
                           List<LineaOrden> lineas, int idRecetaOrigen) {
        this(id, fecha, lineas);
        this.idRecetaOrigen = idRecetaOrigen;
    }

    /**
     * Devuelve el id de la receta cronica que origino esta orden,
     * o 0 si no procede de ninguna receta.
     *
     * @return id de la receta origen, o 0
     */
    public int getIdRecetaOrigen() { return idRecetaOrigen; }

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
        return String.format("[%d] Fecha: %s | Estado: %-10s | Líneas: %d",
                id, fecha, estado, lineas.size());
    }
}

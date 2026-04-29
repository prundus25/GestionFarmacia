package model;

import java.io.Serializable;

/**
 * Línea de detalle de una OrdenReposicion que
 * indica el Medicamento y la cantidad solicitada.
 */
public class LineaOrden implements Serializable {
    private static final long serialVersionUID = 1L;

    private Medicamento medicamento;
    private int cantidadSolicitada;

    /**
     * Crea una nueva LineaOrden.
     *
     * @param medicamento       medicamento a reponer
     * @param cantidadSolicitada unidades solicitadas
     */
    public LineaOrden(Medicamento medicamento,
                      int cantidadSolicitada) {
        this.medicamento = medicamento;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    /**
     * Devuelve el medicamento de esta línea de orden.
     *
     * @return el medicamento
     */
    public Medicamento getMedicamento() { return medicamento; }

    /**
     * Establece el medicamento de esta línea de orden.
     *
     * @param medicamento el nuevo medicamento
     */
    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    /**
     * Devuelve la cantidad de unidades solicitadas.
     *
     * @return la cantidad solicitada
     */
    public int getCantidadSolicitada() {
        return cantidadSolicitada;
    }

    /**
     * Establece la cantidad de unidades solicitadas.
     *
     * @param cantidadSolicitada la nueva cantidad
     */
    public void setCantidadSolicitada(int cantidadSolicitada) {
        this.cantidadSolicitada = cantidadSolicitada;
    }

    /**
     * Devuelve una representación textual de esta línea
     * con formato tabular para su visualización en consola.
     *
     * @return cadena con nombre del medicamento y cantidad
     *         solicitada
     */
    @Override
    public String toString() {
        return String.format("  - %-25s | Cantidad solicitada: %d",
                medicamento.getNombre(), cantidadSolicitada);
    }
}

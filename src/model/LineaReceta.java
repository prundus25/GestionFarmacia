package model;

import java.io.Serializable;

/**
 * Línea de detalle de una {@link Receta} que asocia un
 * {@link Medicamento} con su posología.
 * <p>
 * Cada línea especifica la dosis, la frecuencia de
 * administración, la duración del tratamiento y la
 * cantidad de unidades a dispensar.
 */
public class LineaReceta implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Medicamento prescrito en esta línea. */
    private Medicamento medicamento;

    /** Dosis por toma (por ejemplo, {@code "500mg"}). */
    private String dosis;

    /**
     * Frecuencia de administración
     * (por ejemplo, {@code "cada 8 horas"}).
     */
    private String frecuencia;

    /** Duración del tratamiento en días. */
    private int duracionDias;

    /** Número de unidades a dispensar al paciente. */
    private int cantidad;

    /**
     * Crea una nueva {@code LineaReceta} con todos sus
     * atributos de posología.
     *
     * @param medicamento  medicamento prescrito
     * @param dosis        dosis por toma
     * @param frecuencia   frecuencia de administración
     * @param duracionDias duración del tratamiento en días
     * @param cantidad     unidades a dispensar
     */
    public LineaReceta(Medicamento medicamento, String dosis,
                       String frecuencia, int duracionDias,
                       int cantidad) {
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracionDias = duracionDias;
        this.cantidad = cantidad;
    }

    /**
     * Devuelve el medicamento prescrito en esta línea.
     *
     * @return el medicamento
     */
    public Medicamento getMedicamento() { return medicamento; }

    /**
     * Establece el medicamento prescrito en esta línea.
     *
     * @param medicamento el nuevo medicamento
     */
    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    /**
     * Devuelve la dosis por toma de esta línea.
     *
     * @return la dosis (por ejemplo, {@code "500mg"})
     */
    public String getDosis() { return dosis; }

    /**
     * Establece la dosis por toma de esta línea.
     *
     * @param dosis la nueva dosis
     */
    public void setDosis(String dosis) { this.dosis = dosis; }

    /**
     * Devuelve la frecuencia de administración de esta
     * línea.
     *
     * @return la frecuencia (por ejemplo,
     *         {@code "cada 8 horas"})
     */
    public String getFrecuencia() { return frecuencia; }

    /**
     * Establece la frecuencia de administración de esta
     * línea.
     *
     * @param frecuencia la nueva frecuencia
     */
    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    /**
     * Devuelve la duración del tratamiento en días.
     *
     * @return el número de días de tratamiento
     */
    public int getDuracionDias() { return duracionDias; }

    /**
     * Establece la duración del tratamiento en días.
     *
     * @param duracionDias el nuevo número de días
     */
    public void setDuracionDias(int duracionDias) {
        this.duracionDias = duracionDias;
    }

    /**
     * Devuelve el número de unidades a dispensar.
     *
     * @return la cantidad de unidades
     */
    public int getCantidad() { return cantidad; }

    /**
     * Establece el número de unidades a dispensar.
     *
     * @param cantidad la nueva cantidad
     */
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Devuelve una representación textual de esta línea
     * con formato tabular para su visualización en consola.
     *
     * @return cadena con nombre del medicamento, dosis,
     *         frecuencia, duración y cantidad
     */
    @Override
    public String toString() {
        return String.format(
                "  - %-25s | Dosis: %-8s | Frec.: %-15s"
                + " | %d días | Cant.: %d",
                medicamento.getNombre(), dosis, frecuencia,
                duracionDias, cantidad);
    }
}

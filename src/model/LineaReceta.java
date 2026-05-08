package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Línea de detalle de una Receta que asocia un
 * Medicamento con su posología.
 *
 * Cada línea especifica la dosis en miligramos, las tomas
 * por día, la duración del tratamiento, cada cuántos días
 * se necesita una caja (frecuenciaDias) y la cantidad total
 * de cajas calculada a partir de esos datos.
 */
public class LineaReceta implements Serializable {
    private static final long serialVersionUID = 1L;

    private Medicamento medicamento;
    private int dosisMg;
    private int tomasPorDia;
    private int duracionDias;
    private int frecuenciaDias;
    private int cajas;
    private LocalDate fechaProximaReposicion;

    /**
     * Crea una nueva LineaReceta con todos sus atributos de posología.
     * Las cajas totales se calculan automáticamente como
     * ceil(duracionDias / frecuenciaDias).
     *
     * @param medicamento   medicamento prescrito
     * @param dosisMg       dosis por toma en miligramos
     * @param tomasPorDia   número de tomas al día
     * @param duracionDias  duración del tratamiento en días
     * @param frecuenciaDias cada cuántos días se necesita una nueva caja
     */
    public LineaReceta(Medicamento medicamento, int dosisMg,
                       int tomasPorDia, int duracionDias, int frecuenciaDias) {
        this.medicamento = medicamento;
        this.dosisMg = dosisMg;
        this.tomasPorDia = tomasPorDia;
        this.duracionDias = duracionDias;
        this.frecuenciaDias = frecuenciaDias <= 0 ? 30 : frecuenciaDias;
        this.cajas = (int) Math.ceil((double) duracionDias / this.frecuenciaDias);
        this.fechaProximaReposicion = null;
    }

    /**
     * Devuelve el medicamento prescrito en esta línea.
     *
     * @return el medicamento
     */
    public Medicamento getMedicamento() { return medicamento; }

    /**
     * Devuelve la dosis por toma en miligramos.
     *
     * @return la dosis en mg
     */
    public int getDosisMg() { return dosisMg; }

    /**
     * Devuelve el número de tomas al día.
     *
     * @return tomas por día
     */
    public int getTomasPorDia() { return tomasPorDia; }

    /**
     * Devuelve la duración del tratamiento en días.
     *
     * @return el número de días de tratamiento
     */
    public int getDuracionDias() { return duracionDias; }

    /**
     * Devuelve cuántos días dura cada caja de este medicamento
     * según la frecuencia indicada en la receta.
     *
     * @return días por caja
     */
    public int getFrecuenciaDias() { return frecuenciaDias; }

    /**
     * Devuelve el número total de cajas a dispensar durante
     * todo el tratamiento.
     *
     * @return el número de cajas
     */
    public int getCajas() { return cajas; }

    /**
     * Devuelve la fecha prevista para la próxima reposición
     * de este medicamento crónico.
     *
     * @return la fecha de próxima reposición, o null si no aplica
     */
    public LocalDate getFechaProximaReposicion() { return fechaProximaReposicion; }

    /**
     * Establece la fecha prevista para la próxima reposición.
     *
     * @param fechaProximaReposicion la nueva fecha de próxima reposición
     */
    public void setFechaProximaReposicion(LocalDate fechaProximaReposicion) {
        this.fechaProximaReposicion = fechaProximaReposicion;
    }

    /**
     * Devuelve una representación textual de esta línea
     * con formato tabular para su visualización en consola.
     *
     * @return cadena con nombre del medicamento, dosis,
     *         tomas por día, duración y cajas
     */
    @Override
    public String toString() {
        return String.format("  - %-25s | Dosis: %dmg | %d toma(s)/d\u00eda | %d d\u00edas | Cajas: %d",
                medicamento.getNombre(), dosisMg, tomasPorDia, duracionDias, cajas);
    }
}



package model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Línea de detalle de una Receta que asocia un
 * Medicamento con su posología.
 * 
 * Cada línea especifica la dosis en miligramos, las tomas
 * por día, la duración del tratamiento y la cantidad de
 * cajas a dispensar.
 */
public class LineaReceta implements Serializable {
    private static final long serialVersionUID = 1L;

    private Medicamento medicamento;
    private int dosisMg;
    private int tomasPorDia;
    private int duracionDias;
    private int cajas;
    private int cajasRestantes;
    private int duracionCaja;
    private LocalDate fechaProximaReposicion;
    private boolean ordenReposicionGenerada;
    private boolean proximaCajaReservada;

    /**
     * Crea una nueva LineaReceta con todos sus atributos de posología.
     * Las cajas necesarias se calculan automáticamente a partir de
     * la duración, las tomas por día y las unidades por caja del medicamento.
     *
     * @param medicamento  medicamento prescrito
     * @param dosisMg      dosis por toma en miligramos
     * @param tomasPorDia  número de tomas al día
     * @param duracionDias duración del tratamiento en días
     */
    public LineaReceta(Medicamento medicamento, int dosisMg,
                       int tomasPorDia, int duracionDias) {
        this.medicamento = medicamento;
        this.dosisMg = dosisMg;
        this.tomasPorDia = tomasPorDia;
        this.duracionDias = duracionDias;
        int unidades = medicamento.getUnidadesPorCaja();
        this.cajas = (tomasPorDia <= 0 || unidades <= 0) ? 1
                : (int) Math.ceil((double) duracionDias * tomasPorDia / unidades);
        this.cajasRestantes = 0;
        this.duracionCaja = 0;
        this.ordenReposicionGenerada = false;
        this.proximaCajaReservada = false;
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
     * Devuelve la dosis por toma en miligramos.
     *
     * @return la dosis en mg
     */
    public int getDosisMg() { return dosisMg; }

    /**
     * Establece la dosis por toma en miligramos.
     *
     * @param dosisMg la nueva dosis en mg
     */
    public void setDosisMg(int dosisMg) { this.dosisMg = dosisMg; }

    /**
     * Devuelve el número de tomas al día.
     *
     * @return tomas por día
     */
    public int getTomasPorDia() { return tomasPorDia; }

    /**
     * Establece el número de tomas al día.
     *
     * @param tomasPorDia el nuevo número de tomas
     */
    public void setTomasPorDia(int tomasPorDia) {
        this.tomasPorDia = tomasPorDia;
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
     * Devuelve el número de cajas a dispensar, calculado
     * automáticamente en el constructor a partir de la posología.
     *
     * @return el número de cajas
     */
    public int getCajas() { return cajas; }

    /**
     * Devuelve las cajas que quedan por entregar en este medicamento crónico.
     *
     * @return cajas restantes
     */
    public int getCajasRestantes() { return cajasRestantes; }

    /**
     * Establece las cajas restantes por entregar.
     *
     * @param cajasRestantes el nuevo número de cajas
     */
    public void setCajasRestantes(int cajasRestantes) {
        this.cajasRestantes = cajasRestantes;
    }

    /**
     * Devuelve los días que dura una caja según la posología de esta línea.
     *
     * @return días por caja
     */
    public int getDuracionCaja() { return duracionCaja; }

    /**
     * Establece los días que dura una caja.
     *
     * @param duracionCaja el número de días por caja
     */
    public void setDuracionCaja(int duracionCaja) {
        this.duracionCaja = duracionCaja;
    }

    /**
     * Devuelve la fecha prevista para la próxima reposición de este medicamento.
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
     * Indica si ya se ha generado una orden de reposición para
     * el próximo ciclo de este medicamento crónico.
     *
     * @return true si la orden ya fue generada
     */
    public boolean isOrdenReposicionGenerada() { return ordenReposicionGenerada; }

    /**
     * Establece si la orden de reposición del próximo ciclo ya fue generada.
     *
     * @param ordenReposicionGenerada true si la orden ya existe
     */
    public void setOrdenReposicionGenerada(boolean ordenReposicionGenerada) {
        this.ordenReposicionGenerada = ordenReposicionGenerada;
    }

    /**
     * Indica si la caja del próximo ciclo crónico está reservada en el
     * inventario y no puede ser tomada por otras recetas.
     *
     * @return true si hay una caja reservada para este ciclo
     */
    public boolean isProximaCajaReservada() { return proximaCajaReservada; }

    /**
     * Establece si la caja del próximo ciclo está reservada.
     *
     * @param proximaCajaReservada true para marcar la caja como reservada
     */
    public void setProximaCajaReservada(boolean proximaCajaReservada) {
        this.proximaCajaReservada = proximaCajaReservada;
    }

    /**
     * Calcula los días que dura una caja de este medicamento
     * con la posología indicada en esta línea.
     *
     * @return días por caja; 1 como mínimo para evitar división por cero
     */
    public int calcularDiasPorCaja() {
        if (tomasPorDia <= 0) return 1;
        int unidades = medicamento.getUnidadesPorCaja();
        return Math.max(1, unidades / tomasPorDia);
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
        return String.format("  - %-25s | Dosis: %dmg | %d toma(s)/día | %d días | Cajas: %d",
                medicamento.getNombre(), dosisMg, tomasPorDia, duracionDias, cajas);
    }
}

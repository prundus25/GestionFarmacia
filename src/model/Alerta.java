package model;

import model.enums.TipoAlerta;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa una alerta generada automáticamente por el
 * sistema ante una situación que requiere atención.
 * 
 * Las alertas pueden ser de dos tipos: caducidad próxima
 * de un medicamento (CADUCIDAD) o stock
 * igual o inferior al mínimo configurado
 * (STOCK_MINIMO). Una alerta se crea
 * en estado no resuelta y puede marcarse como resuelta
 * manualmente desde el menú de alertas.
 */
public class Alerta implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private TipoAlerta tipo;
    private Medicamento medicamento;
    private LocalDate fecha;
    private boolean resuelta;

    /**
     * Crea una nueva Alerta en estado no resuelta.
     *
     * @param id          identificador único asignado por el sistema
     * @param tipo        tipo de alerta
     * @param medicamento medicamento afectado
     * @param fecha       fecha de generación de la alerta
     */
    public Alerta(int id, TipoAlerta tipo,
                  Medicamento medicamento, LocalDate fecha) {
        this.id = id;
        this.tipo = tipo;
        this.medicamento = medicamento;
        this.fecha = fecha;
        this.resuelta = false;
    }

    /**
     * Devuelve el identificador único de esta alerta.
     *
     * @return el identificador único
     */
    public int getId() { return id; }

    /**
     * Establece el identificador único de esta alerta.
     *
     * @param id el nuevo identificador
     */
    public void setId(int id) { this.id = id; }

    /**
     * Devuelve el tipo de esta alerta.
     *
     * @return el tipo de alerta
     */
    public TipoAlerta getTipo() { return tipo; }

    /**
     * Establece el tipo de esta alerta.
     *
     * @param tipo el nuevo tipo
     */
    public void setTipo(TipoAlerta tipo) { this.tipo = tipo; }

    /**
     * Devuelve el medicamento afectado por esta alerta.
     *
     * @return el medicamento afectado
     */
    public Medicamento getMedicamento() { return medicamento; }

    /**
     * Establece el medicamento afectado por esta alerta.
     *
     * @param medicamento el nuevo medicamento
     */
    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    /**
     * Devuelve la fecha de generación de esta alerta.
     *
     * @return la fecha de la alerta
     */
    public LocalDate getFecha() { return fecha; }

    /**
     * Establece la fecha de generación de esta alerta.
     *
     * @param fecha la nueva fecha
     */
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    /**
     * Indica si esta alerta ya ha sido resuelta.
     *
     * @return true si ha sido resuelta;
     *         false si sigue activa
     */
    public boolean isResuelta() { return resuelta; }

    /**
     * Establece si esta alerta ha sido resuelta.
     *
     * @param resuelta true para marcarla como
     *                 resuelta
     */
    public void setResuelta(boolean resuelta) {
        this.resuelta = resuelta;
    }

    /**
     * Devuelve una representación textual de la alerta con
     * formato tabular para su visualización en consola.
     *
     * @return cadena con id, tipo, nombre del medicamento,
     *         detalle, fecha y estado de resolución
     */
    @Override
    public String toString() {
        String detalle;
        if (tipo == TipoAlerta.CADUCIDAD) {
            detalle = "Caduca el: " + medicamento.getFechaCaducidad()
                    + " | Stock: " + medicamento.getStock()
                    + " (mín: " + medicamento.getStockMinimo() + ")";
        } else {
            detalle = "Stock actual: " + medicamento.getStock()
                    + " (mín: " + medicamento.getStockMinimo() + ")";
        }
        return String.format("[%d] %-14s | [Med.%d] %-21s | %s | %s | %s",
                id, tipo, medicamento.getId(), medicamento.getNombre(),
                detalle, fecha,
                resuelta ? "RESUELTA" : "ACTIVA");
    }
}

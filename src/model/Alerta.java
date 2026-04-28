package model;

import model.enums.TipoAlerta;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Representa una alerta generada automáticamente por el
 * sistema ante una situación que requiere atención.
 * <p>
 * Las alertas pueden ser de dos tipos: caducidad próxima
 * de un medicamento ({@link TipoAlerta#CADUCIDAD}) o stock
 * igual o inferior al mínimo configurado
 * ({@link TipoAlerta#STOCK_MINIMO}). Una alerta se crea
 * en estado no resuelta y puede marcarse como resuelta
 * manualmente desde el menú de alertas.
 */
public class Alerta implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Identificador único de la alerta en el sistema. */
    private int id;

    /** Tipo de situación que originó esta alerta. */
    private TipoAlerta tipo;

    /** Medicamento afectado por la situación. */
    private Medicamento medicamento;

    /** Fecha en que se generó la alerta. */
    private LocalDate fecha;

    /**
     * Indica si la situación ha sido atendida y la alerta
     * ya no está activa.
     */
    private boolean resuelta;

    /**
     * Crea una nueva {@code Alerta} en estado no resuelta.
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
     * @return {@code true} si ha sido resuelta;
     *         {@code false} si sigue activa
     */
    public boolean isResuelta() { return resuelta; }

    /**
     * Establece si esta alerta ha sido resuelta.
     *
     * @param resuelta {@code true} para marcarla como
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
            detalle = "Caduca el: " + medicamento.getFechaCaducidad();
        } else {
            detalle = "Stock actual: " + medicamento.getStock()
                    + " (mín: " + medicamento.getStockMinimo() + ")";
        }
        return String.format(
                "[%d] %-14s | %-25s | %s | %s | %s",
                id, tipo, medicamento.getNombre(),
                detalle, fecha,
                resuelta ? "RESUELTA" : "ACTIVA");
    }
}

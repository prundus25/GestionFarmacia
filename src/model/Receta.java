package model;

import model.enums.EstadoReceta;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Representa una receta médica emitida dentro del sistema
 * de la farmacia hospitalaria.
 * <p>
 * Vincula a un {@link Paciente} con un {@link Medico} y
 * contiene las líneas de medicamentos prescritos. Al
 * crearse, el estado se fija automáticamente a
 * {@link EstadoReceta#PENDIENTE}; si algún medicamento
 * prescrito es de uso restringido, el menú cambia el
 * estado a {@link EstadoReceta#PENDIENTE_AUTORIZACION}
 * antes de persistir la receta.
 */
public class Receta implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Identificador único de la receta en el sistema. */
    private int id;

    /** Paciente al que va dirigida la receta. */
    private Paciente paciente;

    /** Médico que ha emitido la receta. */
    private Medico medico;

    /**
     * Líneas de medicamentos prescritos en esta receta.
     */
    private List<LineaReceta> lineas;

    /** Fecha de emisión de la receta. */
    private LocalDate fecha;

    /** Estado actual dentro del ciclo de vida. */
    private EstadoReceta estado;

    /**
     * Indica si la receta es crónica y puede renovarse
     * periódicamente.
     */
    private boolean cronica;

    /**
     * Crea una nueva {@code Receta} con estado inicial
     * {@link EstadoReceta#PENDIENTE}.
     *
     * @param id       identificador único asignado por el sistema
     * @param paciente paciente al que se prescribe
     * @param medico   médico que emite la receta
     * @param lineas   líneas de medicamentos prescritos
     * @param fecha    fecha de emisión
     * @param cronica  {@code true} si la receta es crónica
     */
    public Receta(int id, Paciente paciente, Medico medico,
                  List<LineaReceta> lineas,
                  LocalDate fecha, boolean cronica) {
        this.id = id;
        this.paciente = paciente;
        this.medico = medico;
        this.lineas = lineas;
        this.fecha = fecha;
        this.estado = EstadoReceta.PENDIENTE;
        this.cronica = cronica;
    }

    /**
     * Devuelve el identificador único de esta receta.
     *
     * @return el identificador único
     */
    public int getId() { return id; }

    /**
     * Establece el identificador único de esta receta.
     *
     * @param id el nuevo identificador
     */
    public void setId(int id) { this.id = id; }

    /**
     * Devuelve el paciente asociado a esta receta.
     *
     * @return el paciente
     */
    public Paciente getPaciente() { return paciente; }

    /**
     * Establece el paciente asociado a esta receta.
     *
     * @param paciente el nuevo paciente
     */
    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    /**
     * Devuelve el médico que emitió esta receta.
     *
     * @return el médico emisor
     */
    public Medico getMedico() { return medico; }

    /**
     * Establece el médico que emitió esta receta.
     *
     * @param medico el nuevo médico
     */
    public void setMedico(Medico medico) { this.medico = medico; }

    /**
     * Devuelve las líneas de medicamentos prescritos en
     * esta receta.
     *
     * @return lista de líneas de receta
     */
    public List<LineaReceta> getLineas() { return lineas; }

    /**
     * Establece las líneas de medicamentos de esta receta.
     *
     * @param lineas la nueva lista de líneas
     */
    public void setLineas(List<LineaReceta> lineas) {
        this.lineas = lineas;
    }

    /**
     * Devuelve la fecha de emisión de esta receta.
     *
     * @return la fecha de emisión
     */
    public LocalDate getFecha() { return fecha; }

    /**
     * Establece la fecha de emisión de esta receta.
     *
     * @param fecha la nueva fecha de emisión
     */
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    /**
     * Devuelve el estado actual de esta receta.
     *
     * @return el estado dentro del ciclo de vida
     */
    public EstadoReceta getEstado() { return estado; }

    /**
     * Establece el estado de esta receta.
     *
     * @param estado el nuevo estado
     */
    public void setEstado(EstadoReceta estado) {
        this.estado = estado;
    }

    /**
     * Indica si esta receta es crónica.
     *
     * @return {@code true} si la receta es crónica;
     *         {@code false} en caso contrario
     */
    public boolean isCronica() { return cronica; }

    /**
     * Establece si esta receta es crónica.
     *
     * @param cronica {@code true} para marcarla como
     *                crónica
     */
    public void setCronica(boolean cronica) {
        this.cronica = cronica;
    }

    /**
     * Devuelve una representación textual de la receta con
     * formato tabular para su visualización en consola.
     *
     * @return cadena con id, paciente, médico, fecha,
     *         estado e indicador de receta crónica
     */
    @Override
    public String toString() {
        return String.format(
                "[%d] Paciente: %-25s | Médico: %-25s"
                + " | Fecha: %s | Estado: %-25s | %s",
                id, paciente.getNombreCompleto(),
                "Dr/a. " + medico.getNombreCompleto(),
                fecha, estado, cronica ? "[CRÓNICA]" : "");
    }
}

package model;

import model.enums.EstadoReceta;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Representa una receta médica emitida dentro del sistema
 * de la farmacia hospitalaria.
 * 
 * Vincula a un Paciente con un Medico y
 * contiene las líneas de medicamentos prescritos. Al
 * crearse, el estado se fija automáticamente a
 * PENDIENTE si algún medicamento
 * prescrito es de uso restringido, el menú cambia el
 * estado a PENDIENTE_AUTORIZACION antes de persistir la receta.
 */
public class Receta implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private Paciente paciente;
    private Medico medico;
    private List<LineaReceta> lineas;
    private LocalDate fecha;
    private EstadoReceta estado;
    private boolean cronica;

    /**
     * Crea una nueva Receta con estado inicial PENDIENTE.
     *
     * @param id       identificador único asignado por el sistema
     * @param paciente paciente al que se prescribe
     * @param medico   médico que emite la receta
     * @param lineas   líneas de medicamentos prescritos
     * @param fecha    fecha de emisión
     * @param cronica  true si la receta es crónica
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
     * @return true si la receta es crónica;
     *         false en caso contrario
     */
    public boolean isCronica() { return cronica; }

    /**
     * Establece si esta receta es crónica.
     *
     * @param cronica true para marcarla como
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

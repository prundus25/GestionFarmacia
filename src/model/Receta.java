package model;

import model.enums.EstadoReceta;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

/**
 * Representa una receta médica emitida dentro del sistema
 * de la farmacia.
 *
 * Vincula a un Paciente con un Medico y contiene las líneas
 * de medicamentos prescritos. Al crearse el estado se fija
 * automáticamente a PENDIENTE.
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
    private LocalDate fechaDispensacion;

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
        this.estado = EstadoReceta.PENDIENTE_VALIDAR;
        this.cronica = cronica;
    }

    /**
     * Devuelve el identificador único de esta receta.
     *
     * @return el identificador único
     */
    public int getId() { return id; }

    /**
     * Devuelve el paciente asociado a esta receta.
     *
     * @return el paciente
     */
    public Paciente getPaciente() { return paciente; }

    /**
     * Devuelve el médico que emitió esta receta.
     *
     * @return el médico emisor
     */
    public Medico getMedico() { return medico; }

    /**
     * Devuelve las líneas de medicamentos prescritos en esta receta.
     *
     * @return lista de líneas de receta
     */
    public List<LineaReceta> getLineas() { return lineas; }

    /**
     * Devuelve la fecha de emisión de esta receta.
     *
     * @return la fecha de emisión
     */
    public LocalDate getFecha() { return fecha; }

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
     * @return true si la receta es crónica
     */
    public boolean isCronica() { return cronica; }

    /**
     * Devuelve la fecha en que se dispensó esta receta.
     * Solo relevante para recetas crónicas dispensadas.
     *
     * @return la fecha de dispensación, o null si no se ha dispensado
     */
    public LocalDate getFechaDispensacion() { return fechaDispensacion; }

    /**
     * Establece la fecha de dispensación de esta receta.
     *
     * @param fechaDispensacion la fecha en que se entregó
     */
    public void setFechaDispensacion(LocalDate fechaDispensacion) {
        this.fechaDispensacion = fechaDispensacion;
    }

    /**
     * Devuelve una representación textual de la receta con
     * formato tabular para su visualización en consola.
     *
     * @return cadena con id, paciente, médico, fecha, estado e indicador crónico
     */
    @Override
    public String toString() {
        return String.format("[%d] Paciente: %-25s | Médico: Dr/a. %-25s | Fecha: %s | Estado: %-22s | %s",
                id, paciente.getNombreCompleto(), medico.getNombreCompleto(),
                fecha, estado, cronica ? "[CRÓNICA]" : "");
    }
}

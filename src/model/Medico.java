package model;

/**
 * Representa a un médico del hospital.
 * <p>
 * Extiende {@link Persona} añadiendo la especialidad
 * médica y el número de colegiado, que identifican
 * al facultativo en el sistema.
 */
public class Medico extends Persona {
    private static final long serialVersionUID = 1L;

    /** Especialidad médica del facultativo. */
    private String especialidad;

    /** Número de colegiado del facultativo. */
    private String numeroColegiado;

    /**
     * Crea un nuevo {@code Medico} con todos sus atributos.
     *
     * @param id              identificador único asignado por el sistema
     * @param nombre          nombre de pila
     * @param apellidos       apellidos
     * @param especialidad    especialidad médica
     * @param numeroColegiado número de colegiado oficial
     */
    public Medico(int id, String nombre, String apellidos,
                  String especialidad, String numeroColegiado) {
        super(id, nombre, apellidos);
        this.especialidad = especialidad;
        this.numeroColegiado = numeroColegiado;
    }

    /**
     * Devuelve la especialidad médica de este facultativo.
     *
     * @return la especialidad médica
     */
    public String getEspecialidad() { return especialidad; }

    /**
     * Establece la especialidad médica de este facultativo.
     *
     * @param especialidad la nueva especialidad
     */
    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    /**
     * Devuelve el número de colegiado de este facultativo.
     *
     * @return el número de colegiado
     */
    public String getNumeroColegiado() { return numeroColegiado; }

    /**
     * Establece el número de colegiado de este facultativo.
     *
     * @param numeroColegiado el nuevo número de colegiado
     */
    public void setNumeroColegiado(String numeroColegiado) {
        this.numeroColegiado = numeroColegiado;
    }

    /**
     * Devuelve una representación textual del médico con
     * formato tabular para su visualización en consola.
     *
     * @return cadena con id, nombre completo, especialidad
     *         y número de colegiado
     */
    @Override
    public String toString() {
        return String.format("[%d] Dr/a. %-30s | Especialidad: %-20s | Nº Colegiado: %s",
                getId(), getNombreCompleto(), especialidad, numeroColegiado);
    }
}

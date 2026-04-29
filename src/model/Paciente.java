package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un paciente atendido por la farmacia
 * hospitalaria.
 * 
 * Extiende Persona añadiendo datos clínicos:
 * fecha de nacimiento, DNI, lista de alergias y lista
 * de enfermedades crónicas.
 */
public class Paciente extends Persona {
    private static final long serialVersionUID = 1L;

    private LocalDate fechaNacimiento;
    private String dni;
    private List<String> alergias;
    private List<String> enfermedadesCronicas;

    /**
     * Crea un nuevo Paciente con los datos de
     * identificación.
     * 
     * Las listas de alergias y enfermedades crónicas
     * se inicializan vacías.
     *
     * @param id              identificador único asignado por el sistema
     * @param nombre          nombre de pila
     * @param apellidos       apellidos
     * @param fechaNacimiento fecha de nacimiento
     * @param dni             DNI del paciente
     */
    public Paciente(int id, String nombre, String apellidos,
                    LocalDate fechaNacimiento, String dni) {
        super(id, nombre, apellidos);
        this.fechaNacimiento = fechaNacimiento;
        this.dni = dni;
        this.alergias = new ArrayList<>();
        this.enfermedadesCronicas = new ArrayList<>();
    }

    /**
     * Devuelve la fecha de nacimiento de este paciente.
     *
     * @return la fecha de nacimiento
     */
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }

    /**
     * Establece la fecha de nacimiento de este paciente.
     *
     * @param fechaNacimiento la nueva fecha de nacimiento
     */
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    /**
     * Devuelve el DNI de este paciente.
     *
     * @return el DNI
     */
    public String getDni() { return dni; }

    /**
     * Establece el DNI de este paciente.
     *
     * @param dni el nuevo DNI
     */
    public void setDni(String dni) { this.dni = dni; }

    /**
     * Devuelve la lista de alergias de este paciente.
     *
     * @return lista de alergias; puede estar vacía pero
     *         nunca es null
     */
    public List<String> getAlergias() { return alergias; }

    /**
     * Establece la lista de alergias de este paciente.
     *
     * @param alergias la nueva lista de alergias
     */
    public void setAlergias(List<String> alergias) {
        this.alergias = alergias;
    }

    /**
     * Devuelve la lista de enfermedades crónicas de este
     * paciente.
     *
     * @return lista de enfermedades crónicas; puede estar
     *         vacía pero nunca es null
     */
    public List<String> getEnfermedadesCronicas() {
        return enfermedadesCronicas;
    }

    /**
     * Establece la lista de enfermedades crónicas de este
     * paciente.
     *
     * @param enfermedadesCronicas la nueva lista de
     *                             enfermedades crónicas
     */
    public void setEnfermedadesCronicas(
            List<String> enfermedadesCronicas) {
        this.enfermedadesCronicas = enfermedadesCronicas;
    }

    /**
     * Devuelve una representación textual del paciente con
     * formato tabular para su visualización en consola.
     *
     * @return cadena con id, nombre completo, DNI, fecha de
     *         nacimiento, alergias y enfermedades crónicas
     */
    @Override
    public String toString() {
        return String.format("[%d] %-30s | DNI: %-9s | Nac.: %s | Alergias: %s | Enf.Crónicas: %s",
                getId(), getNombreCompleto(), dni, fechaNacimiento,
                alergias.isEmpty() ? "Ninguna" : String.join(", ", alergias),
                enfermedadesCronicas.isEmpty() ? "Ninguna" : String.join(", ", enfermedadesCronicas));
    }
}

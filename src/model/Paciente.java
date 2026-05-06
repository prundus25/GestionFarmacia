package model;

import java.time.LocalDate;

/**
 * Representa a un paciente atendido por la farmacia
 * hospitalaria.
 * 
 * Extiende Persona añadiendo datos de identificación:
 * fecha de nacimiento y DNI.
 */
public class Paciente extends Persona {
    private static final long serialVersionUID = 1L;

    private LocalDate fechaNacimiento;
    private String dni;

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
     * Devuelve una representación textual del paciente con
     * formato tabular para su visualización en consola.
     *
     * @return cadena con id, nombre completo, DNI y fecha de
     *         nacimiento
     */
    @Override
    public String toString() {
        return String.format("[%d] %-30s | DNI: %-9s | Nac.: %s",
                getId(), getNombreCompleto(), dni, fechaNacimiento);
    }
}

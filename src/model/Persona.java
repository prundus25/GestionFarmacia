package model;

import java.io.Serializable;

/**
 * Clase base abstracta que representa a una persona
 * del sistema (médico o paciente).
 * 
 * Proporciona los atributos comunes de identificación
 * (identificador único, nombre y apellidos) y los
 * métodos de acceso correspondientes. No puede
 * instanciarse directamente.
 */
public abstract class Persona implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private String apellidos;

    /**
     * Crea una nueva Persona con los datos básicos
     * de identificación.
     *
     * @param id        identificador único asignado por el sistema
     * @param nombre    nombre de pila
     * @param apellidos apellidos
     */
    public Persona(int id, String nombre, String apellidos) {
        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
    }

    /**
     * Devuelve el identificador único de esta persona.
     *
     * @return el identificador único
     */
    public int getId() { return id; }

    /**
     * Devuelve el nombre de pila de esta persona.
     *
     * @return el nombre de pila
     */
    public String getNombre() { return nombre; }

    /**
     * Devuelve los apellidos de esta persona.
     *
     * @return los apellidos
     */
    public String getApellidos() { return apellidos; }

    /**
     * Devuelve el nombre completo concatenando nombre y
     * apellidos separados por un espacio.
     *
     * @return nombre y apellidos como una sola cadena
     */
    public String getNombreCompleto() {
        return nombre + " " + apellidos;
    }
}

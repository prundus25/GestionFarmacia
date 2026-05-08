package model;

import model.enums.CategoriaMedicamento;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un medicamento del inventario de la farmacia
 * hospitalaria.
 * 
 * Almacena los datos de identificación, clasificación,
 * control de stock, caducidad y posible restricción
 * de dispensación.
 */
public class Medicamento implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private String nombre;
    private CategoriaMedicamento categoria;
    private int stock;
    private int stockMinimo;
    private LocalDate fechaCaducidad;
    private boolean restringido;
    private int dosisMaximaMg;
    private double precioUnitario;
    private List<Integer> alternativas = new ArrayList<>();
    private List<Integer> interaccionesPeligrosas = new ArrayList<>();

    /**
     * Crea un nuevo Medicamento con todos sus atributos.
     *
     * @param id              identificador único asignado por el sistema
     * @param nombre          nombre del medicamento
     * @param categoria       categoría terapéutica
     * @param stock           unidades disponibles iniciales
     * @param stockMinimo     umbral mínimo de stock
     * @param fechaCaducidad  fecha de caducidad
     * @param restringido     true si requiere autorización para dispensarse
     * @param dosisMaximaMg   dosis máxima permitida por toma en miligramos
     * @param precioUnitario  precio unitario en euros
     */
    public Medicamento(int id, String nombre,
                       CategoriaMedicamento categoria,
                       int stock, int stockMinimo,
                       LocalDate fechaCaducidad,
                       boolean restringido,
                       int dosisMaximaMg,
                       double precioUnitario) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.fechaCaducidad = fechaCaducidad;
        this.restringido = restringido;
        this.dosisMaximaMg = dosisMaximaMg;
        this.precioUnitario = precioUnitario;
    }

    /**
     * Devuelve el identificador único de este medicamento.
     *
     * @return el identificador único
     */
    public int getId() { return id; }

    /**
     * Establece el identificador único de este medicamento.
     *
     * @param id el nuevo identificador
     */
    public void setId(int id) { this.id = id; }

    /**
     * Devuelve el nombre de este medicamento.
     *
     * @return el nombre
     */
    public String getNombre() { return nombre; }

    /**
     * Devuelve la categoría terapéutica de este
     * medicamento.
     *
     * @return la categoría terapéutica
     */
    public CategoriaMedicamento getCategoria() { return categoria; }

    /**
     * Devuelve las unidades disponibles actualmente en
     * el almacén.
     *
     * @return el stock actual
     */
    public int getStock() { return stock; }

    /**
     * Establece las unidades disponibles en el almacén.
     *
     * @param stock el nuevo valor de stock
     */
    public void setStock(int stock) { this.stock = stock; }

    /**
     * Devuelve el umbral mínimo de stock de este
     * medicamento.
     *
     * @return el stock mínimo
     */
    public int getStockMinimo() { return stockMinimo; }

    /**
     * Devuelve la fecha de caducidad de este medicamento.
     *
     * @return la fecha de caducidad
     */
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }

    /**
     * Indica si este medicamento es de uso restringido.
     *
     * @return true si requiere autorización para
     *         dispensarse; false en caso contrario
     */
    public boolean isRestringido() { return restringido; }

    /**
     * Devuelve la dosis máxima permitida por toma en miligramos.
     *
     * @return la dosis máxima en mg
     */
    public int getDosisMaximaMg() { return dosisMaximaMg; }

    /**
     * Devuelve el precio unitario de este medicamento en euros.
     *
     * @return el precio unitario
     */
    public double getPrecioUnitario() { return precioUnitario; }

    /**
     * Devuelve la lista de IDs de medicamentos alternativos a este.
     *
     * @return lista mutable de IDs de alternativas
     */
    public List<Integer> getAlternativas() { return alternativas; }

    /**
     * Devuelve la lista de IDs de medicamentos con los que este
     * tiene una interacción peligrosa.
     *
     * @return lista mutable de IDs de interacciones peligrosas
     */
    public List<Integer> getInteraccionesPeligrosas() { return interaccionesPeligrosas; }

    /**
     * Devuelve una representación textual del medicamento
     * con formato tabular para su visualización en consola.
     *
     * @return cadena con id, nombre, categoría, stock,
     *         caducidad, precio e indicador de restricción
     */
    @Override
    public String toString() {
        return String.format("[%d] %-25s | %-20s | Stock: %3d (min: %2d) | %6.2f EUR | Cad.: %s %s",
                id, nombre, categoria, stock, stockMinimo, precioUnitario, fechaCaducidad,
                restringido ? "[RESTRINGIDO]" : "");
    }
}

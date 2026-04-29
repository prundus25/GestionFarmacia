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
 * control de stock, caducidad, precio y posible restricción
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
    private double precioUnitario;
    private boolean restringido;
    private List<Integer> alternativas = new ArrayList<>();

    /**
     * Crea un nuevo Medicamento con todos sus
     * atributos.
     *
     * @param id             identificador único asignado
     *                       por el sistema
     * @param nombre         nombre del medicamento
     * @param categoria      categoría terapéutica
     * @param stock          unidades disponibles iniciales
     * @param stockMinimo    umbral mínimo de stock
     * @param fechaCaducidad fecha de caducidad
     * @param precioUnitario precio por unidad en euros
     * @param restringido    true si requiere
     *                       autorización para dispensarse
     */
    public Medicamento(int id, String nombre,
                       CategoriaMedicamento categoria,
                       int stock, int stockMinimo,
                       LocalDate fechaCaducidad,
                       double precioUnitario,
                       boolean restringido) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.fechaCaducidad = fechaCaducidad;
        this.precioUnitario = precioUnitario;
        this.restringido = restringido;
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
     * Establece el nombre de este medicamento.
     *
     * @param nombre el nuevo nombre
     */
    public void setNombre(String nombre) { this.nombre = nombre; }

    /**
     * Devuelve la categoría terapéutica de este
     * medicamento.
     *
     * @return la categoría terapéutica
     */
    public CategoriaMedicamento getCategoria() { return categoria; }

    /**
     * Establece la categoría terapéutica de este
     * medicamento.
     *
     * @param categoria la nueva categoría terapéutica
     */
    public void setCategoria(CategoriaMedicamento categoria) {
        this.categoria = categoria;
    }

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
     * Establece el umbral mínimo de stock de este
     * medicamento.
     *
     * @param stockMinimo el nuevo umbral mínimo
     */
    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    /**
     * Devuelve la fecha de caducidad de este medicamento.
     *
     * @return la fecha de caducidad
     */
    public LocalDate getFechaCaducidad() { return fechaCaducidad; }

    /**
     * Establece la fecha de caducidad de este medicamento.
     *
     * @param fechaCaducidad la nueva fecha de caducidad
     */
    public void setFechaCaducidad(LocalDate fechaCaducidad) {
        this.fechaCaducidad = fechaCaducidad;
    }

    /**
     * Devuelve el precio unitario de este medicamento
     * en euros.
     *
     * @return el precio unitario
     */
    public double getPrecioUnitario() { return precioUnitario; }

    /**
     * Establece el precio unitario de este medicamento
     * en euros.
     *
     * @param precioUnitario el nuevo precio unitario
     */
    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    /**
     * Indica si este medicamento es de uso restringido.
     *
     * @return true si requiere autorización para
     *         dispensarse; false en caso contrario
     */
    public boolean isRestringido() { return restringido; }

    /**
     * Establece si este medicamento es de uso restringido.
     *
     * @param restringido true para marcar el
     *                    medicamento como restringido
     */
    public void setRestringido(boolean restringido) {
        this.restringido = restringido;
    }

    /**
     * Devuelve la lista de IDs de medicamentos alternativos
     * a este.
     *
     * @return lista mutable de IDs de alternativas
     */
    public List<Integer> getAlternativas() { return alternativas; }

    /**
     * Devuelve una representación textual del medicamento
     * con formato tabular para su visualización en consola.
     *
     * @return cadena con id, nombre, categoría, stock,
     *         caducidad, precio e indicador de restricción
     */
    @Override
    public String toString() {
        return String.format(
                "[%d] %-25s | %-20s | Stock: %3d (min: %2d)"
                + " | Cad.: %s | €%.2f %s",
                id, nombre, categoria, stock, stockMinimo,
                fechaCaducidad, precioUnitario,
                restringido ? "[RESTRINGIDO]" : "");
    }
}

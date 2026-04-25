package model;

import model.enums.CategoriaMedicamento;
import java.io.Serializable;
import java.time.LocalDate;

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

    public Medicamento(int id, String nombre, CategoriaMedicamento categoria,
                       int stock, int stockMinimo, LocalDate fechaCaducidad,
                       double precioUnitario, boolean restringido) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.stock = stock;
        this.stockMinimo = stockMinimo;
        this.fechaCaducidad = fechaCaducidad;
        this.precioUnitario = precioUnitario;
        this.restringido = restringido;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public CategoriaMedicamento getCategoria() { return categoria; }
    public void setCategoria(CategoriaMedicamento categoria) { this.categoria = categoria; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public LocalDate getFechaCaducidad() { return fechaCaducidad; }
    public void setFechaCaducidad(LocalDate fechaCaducidad) { this.fechaCaducidad = fechaCaducidad; }

    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }

    public boolean isRestringido() { return restringido; }
    public void setRestringido(boolean restringido) { this.restringido = restringido; }

    @Override
    public String toString() {
        return String.format("[%d] %-25s | %-20s | Stock: %3d (min: %2d) | Cad.: %s | €%.2f %s",
                id, nombre, categoria, stock, stockMinimo, fechaCaducidad,
                precioUnitario, restringido ? "[RESTRINGIDO]" : "");
    }
}

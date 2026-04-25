package model;

import model.enums.TipoAlerta;
import java.io.Serializable;
import java.time.LocalDate;

public class Alerta implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private TipoAlerta tipo;
    private Medicamento medicamento;
    private LocalDate fecha;
    private boolean resuelta;

    public Alerta(int id, TipoAlerta tipo, Medicamento medicamento, LocalDate fecha) {
        this.id = id;
        this.tipo = tipo;
        this.medicamento = medicamento;
        this.fecha = fecha;
        this.resuelta = false;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public TipoAlerta getTipo() { return tipo; }
    public void setTipo(TipoAlerta tipo) { this.tipo = tipo; }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public boolean isResuelta() { return resuelta; }
    public void setResuelta(boolean resuelta) { this.resuelta = resuelta; }

    @Override
    public String toString() {
        String detalle;
        if (tipo == TipoAlerta.CADUCIDAD) {
            detalle = "Caduca el: " + medicamento.getFechaCaducidad();
        } else {
            detalle = "Stock actual: " + medicamento.getStock() + " (mín: " + medicamento.getStockMinimo() + ")";
        }
        return String.format("[%d] %-14s | %-25s | %s | %s | %s",
                id, tipo, medicamento.getNombre(), detalle, fecha,
                resuelta ? "RESUELTA" : "ACTIVA");
    }
}

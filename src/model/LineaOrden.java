package model;

import java.io.Serializable;

public class LineaOrden implements Serializable {
    private static final long serialVersionUID = 1L;

    private Medicamento medicamento;
    private int cantidadSolicitada;

    public LineaOrden(Medicamento medicamento, int cantidadSolicitada) {
        this.medicamento = medicamento;
        this.cantidadSolicitada = cantidadSolicitada;
    }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public int getCantidadSolicitada() { return cantidadSolicitada; }
    public void setCantidadSolicitada(int cantidadSolicitada) { this.cantidadSolicitada = cantidadSolicitada; }

    @Override
    public String toString() {
        return String.format("  - %-25s | Cantidad solicitada: %d", medicamento.getNombre(), cantidadSolicitada);
    }
}

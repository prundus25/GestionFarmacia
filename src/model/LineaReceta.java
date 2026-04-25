package model;

import java.io.Serializable;

public class LineaReceta implements Serializable {
    private static final long serialVersionUID = 1L;

    private Medicamento medicamento;
    private String dosis;         // ej: "500mg"
    private String frecuencia;    // ej: "cada 8 horas"
    private int duracionDias;
    private int cantidad;         // unidades a dispensar

    public LineaReceta(Medicamento medicamento, String dosis, String frecuencia,
                       int duracionDias, int cantidad) {
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracionDias = duracionDias;
        this.cantidad = cantidad;
    }

    public Medicamento getMedicamento() { return medicamento; }
    public void setMedicamento(Medicamento medicamento) { this.medicamento = medicamento; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public int getDuracionDias() { return duracionDias; }
    public void setDuracionDias(int duracionDias) { this.duracionDias = duracionDias; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return String.format("  - %-25s | Dosis: %-8s | Frec.: %-15s | %d días | Cant.: %d",
                medicamento.getNombre(), dosis, frecuencia, duracionDias, cantidad);
    }
}

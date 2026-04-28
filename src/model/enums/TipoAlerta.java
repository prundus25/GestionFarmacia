package model.enums;

/**
 * Tipo de una {@link model.Alerta} generada por el sistema.
 */
public enum TipoAlerta {

    /**
     * Alerta de caducidad próxima.
     * Se genera cuando la fecha de caducidad de un
     * medicamento es inferior a 30 días a partir de hoy.
     */
    CADUCIDAD,

    /**
     * Alerta de stock mínimo alcanzado.
     * Se genera cuando el stock de un medicamento es
     * igual o inferior a su valor de {@code stockMinimo}.
     */
    STOCK_MINIMO
}

package model.enums;

/**
 * Estado del ciclo de vida de una
 * {@link model.OrdenReposicion}.
 * <p>
 * Una orden se crea en estado {@code PENDIENTE} y solo
 * puede ser aprobada o cancelada desde ese estado.
 */
public enum EstadoOrden {

    /**
     * Orden creada y pendiente de revisión.
     * Es el estado inicial asignado en el constructor
     * de {@link model.OrdenReposicion}.
     */
    PENDIENTE,

    /**
     * Orden aprobada.
     * Al aprobarse, el stock de cada medicamento incluido
     * en la orden se incrementa con la cantidad solicitada.
     */
    APROBADA,

    /** Orden cancelada. El stock no se modifica. */
    CANCELADA
}

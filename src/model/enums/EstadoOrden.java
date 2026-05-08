package model.enums;

/**
 * Estado del ciclo de vida de una OrdenReposicion.
 * 
 * Una orden se crea en estado PENDIENTE y solo
 * puede ser aprobada o cancelada desde ese estado.
 */
public enum EstadoOrden {

    PENDIENTE,
    APROBADA
}

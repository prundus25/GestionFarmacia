package model.enums;

/**
 * Estado del ciclo de vida de una Receta.
 * 
 * Una receta se crea en estado PENDIENTE o
 * PENDIENTE_AUTORIZACION si contiene medicamentos
 * de uso restringido. Solo puede dispensarse desde el
 * estado PENDIENTE.
 */
public enum EstadoReceta {

    PENDIENTE,
    DISPENSADA,
    CANCELADA,
    PENDIENTE_AUTORIZACION
}

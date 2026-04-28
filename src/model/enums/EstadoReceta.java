package model.enums;

/**
 * Estado del ciclo de vida de una {@link model.Receta}.
 * <p>
 * Una receta se crea en estado {@code PENDIENTE} o
 * {@code PENDIENTE_AUTORIZACION} si contiene medicamentos
 * de uso restringido. Solo puede dispensarse desde el
 * estado {@code PENDIENTE}.
 */
public enum EstadoReceta {

    /**
     * Receta creada y pendiente de ser dispensada.
     * Es el estado inicial cuando ningún medicamento
     * prescrito es de uso restringido.
     */
    PENDIENTE,

    /**
     * Receta dispensada correctamente.
     * Los medicamentos han sido entregados al paciente
     * y el stock del inventario ha sido descontado.
     */
    DISPENSADA,

    /** Receta cancelada. No puede ser dispensada. */
    CANCELADA,

    /**
     * Receta pendiente de autorización por el
     * Comité Farmacoterapéutico.
     * Se asigna cuando la receta incluye al menos un
     * medicamento de uso restringido.
     */
    PENDIENTE_AUTORIZACION
}

package model.enums;

/**
 * Estado del ciclo de vida de una Receta.
 * 
 * Una receta comienza en PENDIENTE_VALIDAR. Si incluye medicamentos
 * restringidos pasa a PENDIENTE_AUTORIZACION. Si el farmacéutico
 * detecta interacciones peligrosas o dosis incorrectas pasa a
 * PENDIENTE_MEDICO. Si hay stock insuficiente y sin alternativas pasa a
 * PENDIENTE_STOCK. Una vez validada y con stock confirmado pasa
 * a STOCK_RESERVADO. Finalmente, al entregar el medicamento pasa
 * a DISPENSADA.
 */
public enum EstadoReceta {

    PENDIENTE_VALIDAR,
    PENDIENTE_AUTORIZACION,
    PENDIENTE_MEDICO,
    PENDIENTE_STOCK,
    STOCK_RESERVADO,
    DISPENSADA,
    CANCELADA
}

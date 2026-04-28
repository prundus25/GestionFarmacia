package model.enums;

/**
 * Categoría terapéutica de un medicamento.
 * <p>
 * Se utiliza para clasificar los medicamentos del inventario
 * según su uso clínico principal.
 */
public enum CategoriaMedicamento {

    /** Medicamento con acción analgésica (alivio del dolor). */
    ANALGESICO,

    /** Medicamento antibiótico para el tratamiento de infecciones bacterianas. */
    ANTIBIOTICO,

    /** Medicamento antiinflamatorio para reducir la inflamación. */
    ANTIINFLAMATORIO,

    /** Medicamento antihipertensivo para tratar la hipertensión arterial. */
    ANTIHIPERTENSIVO,

    /** Medicamento anticoagulante para prevenir o tratar coágulos. */
    ANTICOAGULANTE,

    /** Medicamento antidiabético para el control de la glucemia. */
    ANTIDIABETICO,

    /** Medicamento psicotrópico para el tratamiento de trastornos mentales. */
    PSICOFARMACOS,

    /** Medicamento oncológico para el tratamiento del cáncer. */
    ONCOLOGICO,

    /** Medicamento cardiovascular para enfermedades del corazón y vasos. */
    CARDIOVASCULAR,

    /** Medicamento para el tratamiento de enfermedades respiratorias. */
    RESPIRATORIO,

    /** Medicamento para el tratamiento de enfermedades gastrointestinales. */
    GASTROINTESTINAL,

    /** Categoría genérica para medicamentos sin clasificación específica. */
    OTRO
}

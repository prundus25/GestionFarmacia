package ui;

import model.Medico;
import sistema.SistemaFarmacia;

import java.util.ArrayList;

/**
 * Menú de consulta de médicos.
 * Solo permite listar y ver el detalle de los médicos registrados.
 * Los datos de médicos son externos al sistema de farmacia.
 */
public class MenuMedicos {

    private SistemaFarmacia sistema;

    /**
     * Crea un nuevo MenuMedicos asociado al sistema de farmacia dado.
     *
     * @param sistema instancia central del sistema
     */
    public MenuMedicos(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    /**
     * Lanza el bucle interactivo del menú de médicos.
     */
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== MEDICOS =====");
            System.out.println("  1. Listar todos los medicos");
            System.out.println("  2. Ver detalle de medico");
            System.out.println("  0. Volver");
            System.out.println("===================");
            System.out.print("  Opcion: ");

            int opcion = Consola.leerEntero(0, 2);

            switch (opcion) {
                case 1 -> listarTodos();
                case 2 -> verDetalle();
                case 0 -> salir = true;
            }
        }
    }

    private void listarTodos() {
        ArrayList<Medico> lista = sistema.getMedicos();
        System.out.println();
        System.out.println("--- MEDICOS (" + lista.size() + ") ---");
        if (lista.isEmpty()) {
            System.out.println("  No hay medicos registrados.");
        } else {
            for (Medico m : lista) {
                System.out.println("  " + m);
            }
        }
        Consola.pausar();
    }

    private void verDetalle() {
        System.out.print("  ID del medico: ");
        int id = Consola.leerEnteroPositivo();
        Medico m = sistema.buscarMedicoPorId(id);
        if (m == null) {
            System.out.println("  Medico no encontrado.");
            Consola.pausar();
            return;
        }
        System.out.println();
        System.out.println("--- DETALLE DEL MEDICO ---");
        System.out.println("  ID:               " + m.getId());
        System.out.println("  Nombre completo:  Dr/a. " + m.getNombreCompleto());
        System.out.println("  Especialidad:     " + m.getEspecialidad());
        System.out.println("  Nº Colegiado:     " + m.getNumeroColegiado());
        Consola.pausar();
    }
}

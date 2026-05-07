package ui;

import model.Paciente;
import sistema.SistemaFarmacia;
import java.util.ArrayList;

/**
 * Menú de consulta de pacientes.
 * Solo permite listar y ver el detalle de los pacientes registrados.
 * Los datos de pacientes son externos al sistema de farmacia.
 */
public class MenuPacientes {

    private SistemaFarmacia sistema;

    /**
     * Crea un nuevo MenuPacientes asociado al sistema de farmacia dado.
     *
     * @param sistema instancia central del sistema
     */
    public MenuPacientes(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    /**
     * Lanza el bucle interactivo del menú de pacientes.
     */
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== PACIENTES =====");
            System.out.println("  1. Listar todos los pacientes");
            System.out.println("  2. Ver detalle de paciente");
            System.out.println("  0. Volver");
            System.out.println("=====================");
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
        ArrayList<Paciente> lista = sistema.getPacientes();
        System.out.println();
        System.out.println("--- PACIENTES (" + lista.size() + ") ---");
        if (lista.isEmpty()) {
            System.out.println("  No hay pacientes registrados.");
        } else {
            for (Paciente p : lista) {
                System.out.println("  " + p);
            }
        }
        Consola.pausar();
    }

    private void verDetalle() {
        System.out.print("  ID del paciente: ");
        int id = Consola.leerEnteroPositivo();
        Paciente p = sistema.buscarPacientePorId(id);
        if (p == null) {
            System.out.println("  Paciente no encontrado.");
            Consola.pausar();
            return;
        }
        System.out.println();
        System.out.println("--- DETALLE DEL PACIENTE ---");
        System.out.println("  ID:               " + p.getId());
        System.out.println("  Nombre completo:  " + p.getNombreCompleto());
        System.out.println("  DNI:              " + p.getDni());
        System.out.println("  Fecha nacimiento: " + p.getFechaNacimiento());
        Consola.pausar();
    }
}

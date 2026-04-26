package ui;

import model.Medico;
import sistema.SistemaFarmacia;

import java.util.ArrayList;

public class MenuMedicos {

    private SistemaFarmacia sistema;

    public MenuMedicos(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== GESTION DE MEDICOS =====");
            System.out.println("  1. Listar todos los medicos");
            System.out.println("  2. Ver detalle de medico");
            System.out.println("  3. Añadir medico");
            System.out.println("  4. Editar medico");
            System.out.println("  5. Eliminar medico");
            System.out.println("  0. Volver");
            System.out.println("==============================");
            System.out.print("  Opcion: ");

            int opcion = Consola.leerEntero(0, 5);

            switch (opcion) {
                case 1: listarTodos(); break;
                case 2: verDetalle(); break;
                case 3: añadirMedico(); break;
                case 4: editarMedico(); break;
                case 5: eliminarMedico(); break;
                case 0: salir = true; break;
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

    private void añadirMedico() {
        System.out.println();
        System.out.println("--- NUEVO MEDICO ---");

        System.out.print("  Nombre: ");
        String nombre = Consola.scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("  El nombre no puede estar vacio.");
            Consola.pausar();
            return;
        }

        System.out.print("  Apellidos: ");
        String apellidos = Consola.scanner.nextLine().trim();

        System.out.print("  Especialidad: ");
        String especialidad = Consola.scanner.nextLine().trim();

        System.out.print("  Numero de colegiado: ");
        String numeroColegiado = Consola.scanner.nextLine().trim();

        Medico nuevo = new Medico(0, nombre, apellidos, especialidad, numeroColegiado);
        sistema.agregarMedico(nuevo);
        System.out.println("  Medico añadido con ID: " + nuevo.getId());
        Consola.pausar();
    }

    private void editarMedico() {
        System.out.print("  ID del medico a editar: ");
        int id = Consola.leerEnteroPositivo();
        Medico m = sistema.buscarMedicoPorId(id);
        if (m == null) {
            System.out.println("  Medico no encontrado.");
            Consola.pausar();
            return;
        }

        System.out.println("  Editando: Dr/a. " + m.getNombreCompleto() + " (deja en blanco para no cambiar)");

        System.out.print("  Nombre [" + m.getNombre() + "]: ");
        String nombre = Consola.scanner.nextLine().trim();
        if (!nombre.isEmpty()) {
            m.setNombre(nombre);
        }

        System.out.print("  Apellidos [" + m.getApellidos() + "]: ");
        String apellidos = Consola.scanner.nextLine().trim();
        if (!apellidos.isEmpty()) {
            m.setApellidos(apellidos);
        }

        System.out.print("  Especialidad [" + m.getEspecialidad() + "]: ");
        String especialidad = Consola.scanner.nextLine().trim();
        if (!especialidad.isEmpty()) {
            m.setEspecialidad(especialidad);
        }

        System.out.print("  Nº Colegiado [" + m.getNumeroColegiado() + "]: ");
        String colegiado = Consola.scanner.nextLine().trim();
        if (!colegiado.isEmpty()) {
            m.setNumeroColegiado(colegiado);
        }

        System.out.println("  Medico actualizado.");
        Consola.pausar();
    }

    private void eliminarMedico() {
        System.out.print("  ID del medico a eliminar: ");
        int id = Consola.leerEnteroPositivo();
        Medico m = sistema.buscarMedicoPorId(id);
        if (m == null) {
            System.out.println("  Medico no encontrado.");
            Consola.pausar();
            return;
        }

        System.out.print("  ¿Seguro que quieres eliminar a Dr/a. " + m.getNombreCompleto() + "? (s/n): ");
        String conf = Consola.scanner.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) {
            System.out.println("  Cancelado.");
            Consola.pausar();
            return;
        }

        sistema.eliminarMedico(id);
        System.out.println("  Medico eliminado.");
        Consola.pausar();
    }
}

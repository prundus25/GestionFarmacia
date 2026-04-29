package ui;

import model.Paciente;
import sistema.SistemaFarmacia;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Menú de gestión de pacientes.
 * Permite listar, ver detalle, añadir, editar y
 * eliminar pacientes del sistema.
 */
public class MenuPacientes {

    private SistemaFarmacia sistema;

    /**
     * Crea un nuevo MenuPacientes asociado al
     * sistema de farmacia dado.
     *
     * @param sistema instancia central del sistema
     */
    public MenuPacientes(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    /**
     * Lanza el bucle interactivo del menú de pacientes.
     * Continúa mostrando opciones hasta que el usuario
     * elige la opción 0 (Volver).
     */
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== GESTION DE PACIENTES =====");
            System.out.println("  1. Listar todos los pacientes");
            System.out.println("  2. Ver detalle de paciente");
            System.out.println("  3. Añadir paciente");
            System.out.println("  4. Editar paciente");
            System.out.println("  5. Eliminar paciente");
            System.out.println("  0. Volver");
            System.out.println("================================");
            System.out.print("  Opcion: ");

            int opcion = Consola.leerEntero(0, 5);

            switch (opcion) {
                case 1 -> listarTodos();
                case 2 -> verDetalle();
                case 3 -> añadirPaciente();
                case 4 -> editarPaciente();
                case 5 -> eliminarPaciente();
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
        System.out.println("  Alergias:         " + (p.getAlergias().isEmpty() ? "Ninguna" : String.join(", ", p.getAlergias())));
        System.out.println("  Enf. cronicas:    " + (p.getEnfermedadesCronicas().isEmpty() ? "Ninguna" : String.join(", ", p.getEnfermedadesCronicas())));
        Consola.pausar();
    }

    private void añadirPaciente() {
        System.out.println();
        System.out.println("--- NUEVO PACIENTE ---");

        System.out.print("  Nombre: ");
        String nombre = Consola.scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("  El nombre no puede estar vacio.");
            Consola.pausar();
            return;
        }

        System.out.print("  Apellidos: ");
        String apellidos = Consola.scanner.nextLine().trim();

        System.out.print("  DNI: ");
        String dni = Consola.scanner.nextLine().trim();

        System.out.print("  Fecha de nacimiento (dd/MM/yyyy): ");
        LocalDate fechaNacimiento = Consola.leerFecha();

        Paciente nuevo = new Paciente(0, nombre, apellidos, fechaNacimiento, dni);

        System.out.print("  Alergias (separadas por comas, o Enter si no tiene): ");
        String alergiasStr = Consola.scanner.nextLine().trim();
        if (!alergiasStr.isEmpty()) {
            String[] partes = alergiasStr.split(",");
            ArrayList<String> alergias = new ArrayList<>();
            for (String a : partes) {
                if (!a.trim().isEmpty()) {
                    alergias.add(a.trim());
                }
            }
            nuevo.setAlergias(alergias);
        }

        System.out.print("  Enfermedades cronicas (separadas por comas, o Enter si no tiene): ");
        String enfsStr = Consola.scanner.nextLine().trim();
        if (!enfsStr.isEmpty()) {
            String[] partes = enfsStr.split(",");
            ArrayList<String> enfs = new ArrayList<>();
            for (String e : partes) {
                if (!e.trim().isEmpty()) {
                    enfs.add(e.trim());
                }
            }
            nuevo.setEnfermedadesCronicas(enfs);
        }

        sistema.agregarPaciente(nuevo);
        System.out.println("  Paciente añadido con ID: " + nuevo.getId());
        Consola.pausar();
    }

    private void editarPaciente() {
        System.out.print("  ID del paciente a editar: ");
        int id = Consola.leerEnteroPositivo();
        Paciente p = sistema.buscarPacientePorId(id);
        if (p == null) {
            System.out.println("  Paciente no encontrado.");
            Consola.pausar();
            return;
        }

        System.out.println("  Editando: " + p.getNombreCompleto() + " (deja en blanco para no cambiar)");

        System.out.print("  Nombre [" + p.getNombre() + "]: ");
        String nombre = Consola.scanner.nextLine().trim();
        if (!nombre.isEmpty()) {
            p.setNombre(nombre);
        }

        System.out.print("  Apellidos [" + p.getApellidos() + "]: ");
        String apellidos = Consola.scanner.nextLine().trim();
        if (!apellidos.isEmpty()) {
            p.setApellidos(apellidos);
        }

        System.out.print("  DNI [" + p.getDni() + "]: ");
        String dni = Consola.scanner.nextLine().trim();
        if (!dni.isEmpty()) {
            p.setDni(dni);
        }

        System.out.print("  Alergias [" + String.join(", ", p.getAlergias()) + "]: ");
        String alergiasStr = Consola.scanner.nextLine().trim();
        if (!alergiasStr.isEmpty()) {
            String[] partes = alergiasStr.split(",");
            ArrayList<String> alergias = new ArrayList<>();
            for (String a : partes) {
                if (!a.trim().isEmpty()) {
                    alergias.add(a.trim());
                }
            }
            p.setAlergias(alergias);
        }

        System.out.println("  Paciente actualizado.");
        Consola.pausar();
    }

    private void eliminarPaciente() {
        System.out.print("  ID del paciente a eliminar: ");
        int id = Consola.leerEnteroPositivo();
        Paciente p = sistema.buscarPacientePorId(id);
        if (p == null) {
            System.out.println("  Paciente no encontrado.");
            Consola.pausar();
            return;
        }

        System.out.print("  ¿Seguro que quieres eliminar a " + p.getNombreCompleto() + "? (s/n): ");
        String conf = Consola.scanner.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) {
            System.out.println("  Cancelado.");
            Consola.pausar();
            return;
        }

        sistema.eliminarPaciente(id);
        System.out.println("  Paciente eliminado.");
        Consola.pausar();
    }
}

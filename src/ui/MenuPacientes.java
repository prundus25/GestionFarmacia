package ui;

import model.Paciente;
import sistema.SistemaFarmacia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuPacientes {

    private SistemaFarmacia sistema;
    private Scanner scanner;

    public MenuPacientes(SistemaFarmacia sistema) {
        this.sistema = sistema;
        this.scanner = new Scanner(System.in);
    }

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

            int opcion = leerEntero(0, 5);

            switch (opcion) {
                case 1: listarTodos(); break;
                case 2: verDetalle(); break;
                case 3: añadirPaciente(); break;
                case 4: editarPaciente(); break;
                case 5: eliminarPaciente(); break;
                case 0: salir = true; break;
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
        pausar();
    }

    private void verDetalle() {
        System.out.print("  ID del paciente: ");
        int id = leerEnteroPositivo();
        Paciente p = sistema.buscarPacientePorId(id);
        if (p == null) {
            System.out.println("  Paciente no encontrado.");
            pausar();
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
        pausar();
    }

    private void añadirPaciente() {
        System.out.println();
        System.out.println("--- NUEVO PACIENTE ---");

        System.out.print("  Nombre: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("  El nombre no puede estar vacio.");
            pausar();
            return;
        }

        System.out.print("  Apellidos: ");
        String apellidos = scanner.nextLine().trim();

        System.out.print("  DNI: ");
        String dni = scanner.nextLine().trim();

        System.out.print("  Fecha de nacimiento (dd/MM/yyyy): ");
        LocalDate fechaNacimiento = leerFecha();

        Paciente nuevo = new Paciente(0, nombre, apellidos, fechaNacimiento, dni);

        System.out.print("  Alergias (separadas por comas, o Enter si no tiene): ");
        String alergiasStr = scanner.nextLine().trim();
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
        String enfsStr = scanner.nextLine().trim();
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
        pausar();
    }

    private void editarPaciente() {
        System.out.print("  ID del paciente a editar: ");
        int id = leerEnteroPositivo();
        Paciente p = sistema.buscarPacientePorId(id);
        if (p == null) {
            System.out.println("  Paciente no encontrado.");
            pausar();
            return;
        }

        System.out.println("  Editando: " + p.getNombreCompleto() + " (deja en blanco para no cambiar)");

        System.out.print("  Nombre [" + p.getNombre() + "]: ");
        String nombre = scanner.nextLine().trim();
        if (!nombre.isEmpty()) {
            p.setNombre(nombre);
        }

        System.out.print("  Apellidos [" + p.getApellidos() + "]: ");
        String apellidos = scanner.nextLine().trim();
        if (!apellidos.isEmpty()) {
            p.setApellidos(apellidos);
        }

        System.out.print("  DNI [" + p.getDni() + "]: ");
        String dni = scanner.nextLine().trim();
        if (!dni.isEmpty()) {
            p.setDni(dni);
        }

        System.out.print("  Alergias [" + String.join(", ", p.getAlergias()) + "]: ");
        String alergiasStr = scanner.nextLine().trim();
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
        pausar();
    }

    private void eliminarPaciente() {
        System.out.print("  ID del paciente a eliminar: ");
        int id = leerEnteroPositivo();
        Paciente p = sistema.buscarPacientePorId(id);
        if (p == null) {
            System.out.println("  Paciente no encontrado.");
            pausar();
            return;
        }

        System.out.print("  ¿Seguro que quieres eliminar a " + p.getNombreCompleto() + "? (s/n): ");
        String conf = scanner.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) {
            System.out.println("  Cancelado.");
            pausar();
            return;
        }

        sistema.eliminarPaciente(id);
        System.out.println("  Paciente eliminado.");
        pausar();
    }

    // ======= Métodos auxiliares de entrada =======

    private int leerEntero(int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.print("  Valor entre " + min + " y " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero: ");
            }
        }
    }

    private int leerEnteroPositivo() {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= 0) {
                    return valor;
                }
                System.out.print("  Numero positivo: ");
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero: ");
            }
        }
    }

    private LocalDate leerFecha() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine().trim(), fmt);
            } catch (DateTimeParseException e) {
                System.out.print("  Formato incorrecto (dd/MM/yyyy): ");
            }
        }
    }

    private void pausar() {
        System.out.print("\n  Pulsa Enter para continuar...");
        scanner.nextLine();
    }
}

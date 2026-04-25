package ui;

import model.Alerta;
import model.Medicamento;
import model.enums.CategoriaMedicamento;
import model.enums.TipoAlerta;
import sistema.SistemaFarmacia;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuInventario {

    private SistemaFarmacia sistema;
    private Scanner scanner;

    public MenuInventario(SistemaFarmacia sistema) {
        this.sistema = sistema;
        this.scanner = new Scanner(System.in);
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== INVENTARIO DE MEDICAMENTOS =====");
            System.out.println("  1. Listar todos los medicamentos");
            System.out.println("  2. Buscar por nombre");
            System.out.println("  3. Buscar por categoria");
            System.out.println("  4. Ver detalle");
            System.out.println("  5. Añadir medicamento");
            System.out.println("  6. Editar medicamento");
            System.out.println("  7. Eliminar medicamento");
            System.out.println("  0. Volver");
            System.out.println("======================================");
            System.out.print("  Opcion: ");

            int opcion = leerEntero(0, 7);

            switch (opcion) {
                case 1:
                    listarTodos();
                    break;
                case 2:
                    buscarPorNombre();
                    break;
                case 3:
                    buscarPorCategoria();
                    break;
                case 4:
                    verDetalle();
                    break;
                case 5:
                    añadirMedicamento();
                    break;
                case 6:
                    editarMedicamento();
                    break;
                case 7:
                    eliminarMedicamento();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
    }

    private void listarTodos() {
        ArrayList<Medicamento> lista = sistema.getMedicamentos();
        System.out.println();
        System.out.println("--- MEDICAMENTOS (" + lista.size() + ") ---");
        if (lista.isEmpty()) {
            System.out.println("  No hay medicamentos registrados.");
        } else {
            for (Medicamento m : lista) {
                System.out.println("  " + m);
            }
        }
        pausar();
    }

    private void buscarPorNombre() {
        System.out.print("  Nombre a buscar: ");
        String texto = scanner.nextLine().trim().toLowerCase();
        System.out.println();
        System.out.println("--- RESULTADOS ---");
        boolean encontrado = false;
        for (Medicamento m : sistema.getMedicamentos()) {
            if (m.getNombre().toLowerCase().contains(texto)) {
                System.out.println("  " + m);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("  No se encontro ningun medicamento con ese nombre.");
        }
        pausar();
    }

    private void buscarPorCategoria() {
        CategoriaMedicamento cat = seleccionarCategoria();
        System.out.println();
        System.out.println("--- MEDICAMENTOS DE " + cat + " ---");
        boolean encontrado = false;
        for (Medicamento m : sistema.getMedicamentos()) {
            if (m.getCategoria() == cat) {
                System.out.println("  " + m);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("  No hay medicamentos en esta categoria.");
        }
        pausar();
    }

    private void verDetalle() {
        System.out.print("  ID del medicamento: ");
        int id = leerEnteroPositivo();
        Medicamento m = sistema.buscarMedicamentoPorId(id);
        if (m == null) {
            System.out.println("  No se encontro ningun medicamento con ese ID.");
            pausar();
            return;
        }
        System.out.println();
        System.out.println("--- DETALLE ---");
        System.out.println("  ID:           " + m.getId());
        System.out.println("  Nombre:       " + m.getNombre());
        System.out.println("  Categoria:    " + m.getCategoria());
        System.out.println("  Stock:        " + m.getStock());
        System.out.println("  Stock minimo: " + m.getStockMinimo());
        System.out.println("  Caducidad:    " + m.getFechaCaducidad());
        System.out.println("  Precio:       " + m.getPrecioUnitario() + " EUR");
        System.out.println("  Restringido:  " + (m.isRestringido() ? "SI" : "NO"));
        pausar();
    }

    private void añadirMedicamento() {
        System.out.println();
        System.out.println("--- NUEVO MEDICAMENTO ---");

        System.out.print("  Nombre: ");
        String nombre = scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("  El nombre no puede estar vacio.");
            pausar();
            return;
        }

        CategoriaMedicamento categoria = seleccionarCategoria();

        System.out.print("  Stock inicial: ");
        int stock = leerEnteroPositivo();

        System.out.print("  Stock minimo: ");
        int stockMinimo = leerEnteroPositivo();

        System.out.print("  Fecha de caducidad (dd/MM/yyyy): ");
        LocalDate fechaCaducidad = leerFecha();

        System.out.print("  Precio unitario (EUR): ");
        double precio = leerDecimal();

        System.out.print("  ¿Es medicamento restringido? (s/n): ");
        boolean restringido = scanner.nextLine().trim().equalsIgnoreCase("s");

        // Creamos el medicamento con ID 0, el sistema le asignara el ID correcto
        Medicamento nuevo = new Medicamento(0, nombre, categoria, stock, stockMinimo,
                fechaCaducidad, precio, restringido);
        sistema.agregarMedicamento(nuevo);

        // Comprobamos si hay que generar alertas para el nuevo medicamento
        LocalDate hoy = LocalDate.now();
        if (fechaCaducidad.isBefore(hoy.plusDays(30))) {
            Alerta a = new Alerta(0, TipoAlerta.CADUCIDAD, nuevo, hoy);
            sistema.agregarAlerta(a);
            System.out.println("  [AVISO] Se ha generado una alerta de caducidad.");
        }
        if (stock <= stockMinimo) {
            Alerta a = new Alerta(0, TipoAlerta.STOCK_MINIMO, nuevo, hoy);
            sistema.agregarAlerta(a);
            System.out.println("  [AVISO] Se ha generado una alerta de stock minimo.");
        }

        System.out.println("  Medicamento añadido con ID: " + nuevo.getId());
        pausar();
    }

    private void editarMedicamento() {
        System.out.print("  ID del medicamento a editar: ");
        int id = leerEnteroPositivo();
        Medicamento m = sistema.buscarMedicamentoPorId(id);
        if (m == null) {
            System.out.println("  Medicamento no encontrado.");
            pausar();
            return;
        }

        System.out.println("  Editando: " + m.getNombre() + " (deja en blanco para no cambiar)");

        System.out.print("  Nombre [" + m.getNombre() + "]: ");
        String nombre = scanner.nextLine().trim();
        if (!nombre.isEmpty()) {
            m.setNombre(nombre);
        }

        System.out.print("  Stock [" + m.getStock() + "]: ");
        String stockStr = scanner.nextLine().trim();
        if (!stockStr.isEmpty()) {
            try {
                m.setStock(Integer.parseInt(stockStr));
            } catch (NumberFormatException e) {
                System.out.println("  Valor invalido, no se cambio el stock.");
            }
        }

        System.out.print("  Stock minimo [" + m.getStockMinimo() + "]: ");
        String stockMinStr = scanner.nextLine().trim();
        if (!stockMinStr.isEmpty()) {
            try {
                m.setStockMinimo(Integer.parseInt(stockMinStr));
            } catch (NumberFormatException e) {
                System.out.println("  Valor invalido, no se cambio el stock minimo.");
            }
        }

        System.out.print("  Fecha caducidad [" + m.getFechaCaducidad() + "] (dd/MM/yyyy): ");
        String fechaStr = scanner.nextLine().trim();
        if (!fechaStr.isEmpty()) {
            try {
                m.setFechaCaducidad(LocalDate.parse(fechaStr, DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            } catch (DateTimeParseException e) {
                System.out.println("  Formato incorrecto, no se cambio la fecha.");
            }
        }

        System.out.print("  Precio [" + m.getPrecioUnitario() + "]: ");
        String precioStr = scanner.nextLine().trim();
        if (!precioStr.isEmpty()) {
            try {
                m.setPrecioUnitario(Double.parseDouble(precioStr.replace(",", ".")));
            } catch (NumberFormatException e) {
                System.out.println("  Valor invalido, no se cambio el precio.");
            }
        }

        System.out.print("  Restringido [" + (m.isRestringido() ? "s" : "n") + "] (s/n): ");
        String restStr = scanner.nextLine().trim();
        if (restStr.equalsIgnoreCase("s")) {
            m.setRestringido(true);
        } else if (restStr.equalsIgnoreCase("n")) {
            m.setRestringido(false);
        }

        System.out.println("  Medicamento actualizado.");
        pausar();
    }

    private void eliminarMedicamento() {
        System.out.print("  ID del medicamento a eliminar: ");
        int id = leerEnteroPositivo();
        Medicamento m = sistema.buscarMedicamentoPorId(id);
        if (m == null) {
            System.out.println("  Medicamento no encontrado.");
            pausar();
            return;
        }

        System.out.print("  ¿Seguro que quieres eliminar '" + m.getNombre() + "'? (s/n): ");
        String conf = scanner.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) {
            System.out.println("  Cancelado.");
            pausar();
            return;
        }

        sistema.eliminarMedicamento(id);
        System.out.println("  Medicamento eliminado.");
        pausar();
    }

    private CategoriaMedicamento seleccionarCategoria() {
        CategoriaMedicamento[] categorias = CategoriaMedicamento.values();
        System.out.println("  Categorias:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.println("    " + (i + 1) + ". " + categorias[i]);
        }
        System.out.print("  Elige categoria (numero): ");
        return categorias[leerEntero(1, categorias.length) - 1];
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
                System.out.print("  Escribe un numero positivo: ");
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero: ");
            }
        }
    }

    private double leerDecimal() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero decimal: ");
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

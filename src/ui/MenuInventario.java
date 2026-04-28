package ui;

import model.Alerta;
import model.Medicamento;
import model.enums.CategoriaMedicamento;
import model.enums.TipoAlerta;
import sistema.SistemaFarmacia;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Menú de gestión del inventario de medicamentos.
 * <p>
 * Permite listar, buscar, ver detalle, añadir, editar
 * y eliminar medicamentos. Al añadir un medicamento
 * también genera las alertas pertinentes si el stock
 * inicial es bajo o la fecha de caducidad es próxima.
 */
public class MenuInventario {

    private SistemaFarmacia sistema;

    /**
     * Crea un nuevo {@code MenuInventario} asociado al
     * sistema de farmacia dado.
     *
     * @param sistema instancia central del sistema
     */
    public MenuInventario(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    /**
     * Lanza el bucle interactivo del menú de inventario.
     * <p>
     * Continúa mostrando opciones hasta que el usuario
     * elige la opción {@code 0} (Volver).
     */
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

            int opcion = Consola.leerEntero(0, 7);

            switch (opcion) {
                case 1 -> listarTodos();
                case 2 -> buscarPorNombre();
                case 3 -> buscarPorCategoria();
                case 4 -> verDetalle();
                case 5 -> añadirMedicamento();
                case 6 -> editarMedicamento();
                case 7 -> eliminarMedicamento();
                case 0 -> salir = true;
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
        Consola.pausar();
    }

    private void buscarPorNombre() {
        System.out.print("  Nombre a buscar: ");
        String texto = Consola.scanner.nextLine().trim().toLowerCase();
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
        Consola.pausar();
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
        Consola.pausar();
    }

    private void verDetalle() {
        System.out.print("  ID del medicamento: ");
        int id = Consola.leerEnteroPositivo();
        Medicamento m = sistema.buscarMedicamentoPorId(id);
        if (m == null) {
            System.out.println("  No se encontro ningun medicamento con ese ID.");
            Consola.pausar();
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
        Consola.pausar();
    }

    private void añadirMedicamento() {
        System.out.println();
        System.out.println("--- NUEVO MEDICAMENTO ---");

        System.out.print("  Nombre: ");
        String nombre = Consola.scanner.nextLine().trim();
        if (nombre.isEmpty()) {
            System.out.println("  El nombre no puede estar vacio.");
            Consola.pausar();
            return;
        }

        CategoriaMedicamento categoria = seleccionarCategoria();

        System.out.print("  Stock inicial: ");
        int stock = Consola.leerEnteroPositivo();

        System.out.print("  Stock minimo: ");
        int stockMinimo = Consola.leerEnteroPositivo();

        System.out.print("  Fecha de caducidad (dd/MM/yyyy): ");
        LocalDate fechaCaducidad = Consola.leerFecha();

        System.out.print("  Precio unitario (EUR): ");
        double precio = Consola.leerDecimal();

        System.out.print("  ¿Es medicamento restringido? (s/n): ");
        boolean restringido = Consola.scanner.nextLine().trim().equalsIgnoreCase("s");

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
        Consola.pausar();
    }

    private void editarMedicamento() {
        System.out.print("  ID del medicamento a editar: ");
        int id = Consola.leerEnteroPositivo();
        Medicamento m = sistema.buscarMedicamentoPorId(id);
        if (m == null) {
            System.out.println("  Medicamento no encontrado.");
            Consola.pausar();
            return;
        }

        System.out.println("  Editando: " + m.getNombre() + " (deja en blanco para no cambiar)");

        System.out.print("  Stock [" + m.getStock() + "]: ");
        String stockStr = Consola.scanner.nextLine().trim();
        if (!stockStr.isEmpty()) {
            try {
                m.setStock(Integer.parseInt(stockStr));
            } catch (NumberFormatException e) {
                System.out.println("  Valor invalido, no se cambio el stock.");
            }
        }

        System.out.print("  Stock minimo [" + m.getStockMinimo() + "]: ");
        String stockMinStr = Consola.scanner.nextLine().trim();
        if (!stockMinStr.isEmpty()) {
            try {
                m.setStockMinimo(Integer.parseInt(stockMinStr));
            } catch (NumberFormatException e) {
                System.out.println("  Valor invalido, no se cambio el stock minimo.");
            }
        }

        System.out.print("  Precio [" + m.getPrecioUnitario() + "]: ");
        String precioStr = Consola.scanner.nextLine().trim();
        if (!precioStr.isEmpty()) {
            try {
                m.setPrecioUnitario(Double.parseDouble(precioStr.replace(",", ".")));
            } catch (NumberFormatException e) {
                System.out.println("  Valor invalido, no se cambio el precio.");
            }
        }

        System.out.print("  Restringido [" + (m.isRestringido() ? "s" : "n") + "] (s/n): ");
        String restStr = Consola.scanner.nextLine().trim();
        if (restStr.equalsIgnoreCase("s")) {
            m.setRestringido(true);
        } else if (restStr.equalsIgnoreCase("n")) {
            m.setRestringido(false);
        }

        System.out.println("  Medicamento actualizado.");
        Consola.pausar();
    }

    private void eliminarMedicamento() {
        System.out.print("  ID del medicamento a eliminar: ");
        int id = Consola.leerEnteroPositivo();
        Medicamento m = sistema.buscarMedicamentoPorId(id);
        if (m == null) {
            System.out.println("  Medicamento no encontrado.");
            Consola.pausar();
            return;
        }

        System.out.print("  ¿Seguro que quieres eliminar '" + m.getNombre() + "'? (s/n): ");
        String conf = Consola.scanner.nextLine().trim();
        if (!conf.equalsIgnoreCase("s")) {
            System.out.println("  Cancelado.");
            Consola.pausar();
            return;
        }

        sistema.eliminarMedicamento(id);
        System.out.println("  Medicamento eliminado.");
        Consola.pausar();
    }

    private CategoriaMedicamento seleccionarCategoria() {
        CategoriaMedicamento[] categorias = CategoriaMedicamento.values();
        System.out.println("  Categorias:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.println("    " + (i + 1) + ". " + categorias[i]);
        }
        System.out.print("  Elige categoria (numero): ");
        return categorias[Consola.leerEntero(1, categorias.length) - 1];
    }
}

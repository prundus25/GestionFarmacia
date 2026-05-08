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
 * Permite listar, buscar, ver detalle, añadir y editar
 * medicamentos. Al añadir un medicamento también genera
 * las alertas pertinentes si el stock inicial es bajo
 * o la fecha de caducidad es próxima.
 */
public class MenuInventario {

    private SistemaFarmacia sistema;

    /**
     * Crea un nuevo MenuInventario asociado al
     * sistema de farmacia dado.
     *
     * @param sistema instancia central del sistema
     */
    public MenuInventario(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    /**
     * Lanza el bucle interactivo del menú de inventario.
     * 
     * Continúa mostrando opciones hasta que el usuario
     * elige la opción 0 (Volver).
     */
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== INVENTARIO DE MEDICAMENTOS =====");
            System.out.println("  1. Listar todos los medicamentos");
            System.out.println("  2. Ver detalle");
            System.out.println("  3. Añadir medicamento");
            System.out.println("  4. Editar stock");
            System.out.println("  0. Volver");
            System.out.println("======================================");
            System.out.print("  Opcion: ");

            int opcion = Consola.leerEntero(0, 4);

            switch (opcion) {
                case 1 -> listarTodos();
                case 2 -> verDetalle();
                case 3 -> añadirMedicamento();
                case 4 -> editarMedicamento();
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
        System.out.println("  ID:              " + m.getId());
        System.out.println("  Nombre:          " + m.getNombre());
        System.out.println("  Categoria:       " + m.getCategoria());
        System.out.println("  Stock real:      " + m.getStock());
        System.out.println("  Stock reservado: " + sistema.calcularStockReservado(m.getId()));
        System.out.println("  Stock disponible:" + sistema.calcularStockDisponible(m.getId()));
        System.out.println("  Stock minimo:    " + m.getStockMinimo());
        System.out.println("  Caducidad:       " + m.getFechaCaducidad());
        System.out.println("  Dosis maxima:    " + m.getDosisMaximaMg() + "mg");
        System.out.println("  Precio unitario: " + String.format("%.2f", m.getPrecioUnitario()) + " EUR");
        System.out.println("  Restringido:     " + (m.isRestringido() ? "SI" : "NO"));
        if (!m.getAlternativas().isEmpty()) {
            System.out.println("  Alternativas:");
            for (int altId : m.getAlternativas()) {
                Medicamento alt = sistema.buscarMedicamentoPorId(altId);
                if (alt != null) System.out.println("    [" + alt.getId() + "] " + alt.getNombre());
            }
        } else {
            System.out.println("  Alternativas:    ninguna registrada");
        }
        if (!m.getInteraccionesPeligrosas().isEmpty()) {
            System.out.println("  Interacciones peligrosas:");
            for (int intId : m.getInteraccionesPeligrosas()) {
                Medicamento inter = sistema.buscarMedicamentoPorId(intId);
                if (inter != null) System.out.println("    [" + inter.getId() + "] " + inter.getNombre());
            }
        } else {
            System.out.println("  Interacciones:   ninguna registrada");
        }
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

        System.out.print("  ¿Es medicamento restringido? (s/n): ");
        boolean restringido = Consola.scanner.nextLine().trim().equalsIgnoreCase("s");

        System.out.print("  Dosis maxima permitida (mg): ");
        int dosisMaxima = Consola.leerEnteroPositivo();

        System.out.print("  Precio unitario (EUR): ");
        double precio = Consola.leerDecimal();

        Medicamento nuevo = new Medicamento(0, nombre, categoria, stock, stockMinimo,
                fechaCaducidad, restringido, dosisMaxima, precio);
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

        System.out.print("  ¿Desea añadir alternativas? (s/n): ");
        boolean añadirAlt = Consola.scanner.nextLine().trim().equalsIgnoreCase("s");
        while (añadirAlt) {
            System.out.print("  ID del medicamento alternativo (use opcion 1 para ver IDs): ");
            int idAlt = Consola.leerEnteroPositivo();
            if (idAlt == nuevo.getId()) {
                System.out.println("  Un medicamento no puede ser alternativa de si mismo.");
            } else {
                Medicamento alt = sistema.buscarMedicamentoPorId(idAlt);
                if (alt == null) {
                    System.out.println("  Medicamento no encontrado.");
                } else {
                    sistema.añadirAlternativa(nuevo.getId(), idAlt);
                    System.out.println("  Alternativa añadida: " + nuevo.getNombre() + " <-> " + alt.getNombre());
                }
            }
            System.out.print("  ¿Añadir otra alternativa? (s/n): ");
            añadirAlt = Consola.scanner.nextLine().trim().equalsIgnoreCase("s");
        }
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

        System.out.println("  Medicamento: " + m.getNombre() + " | Stock actual: " + m.getStock());
        System.out.print("  Nuevo stock: ");
        int nuevoStock = Consola.leerEnteroPositivo();
        m.setStock(nuevoStock);
        System.out.println("  Stock actualizado a " + nuevoStock + ".");
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

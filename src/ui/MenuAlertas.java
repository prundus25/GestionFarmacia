package ui;

import model.Alerta;
import model.LineaOrden;
import model.Medicamento;
import model.OrdenReposicion;
import model.enums.EstadoOrden;
import model.enums.TipoAlerta;
import sistema.SistemaFarmacia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuAlertas {

    private SistemaFarmacia sistema;
    private Scanner scanner;

    public MenuAlertas(SistemaFarmacia sistema) {
        this.sistema = sistema;
        this.scanner = new Scanner(System.in);
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== ALERTAS Y REPOSICIONES =====");
            System.out.println("  1. Ver alertas activas");
            System.out.println("  2. Ver todas las alertas");
            System.out.println("  3. Marcar alerta como resuelta");
            System.out.println("  4. Generar orden de reposicion");
            System.out.println("  5. Ver ordenes de reposicion");
            System.out.println("  6. Aprobar orden de reposicion");
            System.out.println("  7. Cancelar orden de reposicion");
            System.out.println("  0. Volver");
            System.out.println("==================================");
            System.out.print("  Opcion: ");

            int opcion = leerEntero(0, 7);

            switch (opcion) {
                case 1: verAlertasActivas(); break;
                case 2: verTodasLasAlertas(); break;
                case 3: resolverAlerta(); break;
                case 4: generarOrden(); break;
                case 5: verOrdenes(); break;
                case 6: aprobarOrden(); break;
                case 7: cancelarOrden(); break;
                case 0: salir = true; break;
            }
        }
    }

    private void verAlertasActivas() {
        ArrayList<Alerta> activas = sistema.getAlertasActivas();
        System.out.println();
        System.out.println("--- ALERTAS ACTIVAS (" + activas.size() + ") ---");
        if (activas.isEmpty()) {
            System.out.println("  No hay alertas activas.");
        } else {
            for (Alerta a : activas) {
                System.out.println("  " + a);
            }
        }
        pausar();
    }

    private void verTodasLasAlertas() {
        ArrayList<Alerta> todas = sistema.getAlertas();
        System.out.println();
        System.out.println("--- TODAS LAS ALERTAS (" + todas.size() + ") ---");
        if (todas.isEmpty()) {
            System.out.println("  No hay alertas registradas.");
        } else {
            for (Alerta a : todas) {
                System.out.println("  " + a);
            }
        }
        pausar();
    }

    private void resolverAlerta() {
        System.out.print("  ID de la alerta a resolver: ");
        int id = leerEnteroPositivo();

        // Buscamos la alerta en la lista
        Alerta alertaEncontrada = null;
        for (Alerta a : sistema.getAlertas()) {
            if (a.getId() == id) {
                alertaEncontrada = a;
                break;
            }
        }

        if (alertaEncontrada == null) {
            System.out.println("  Alerta no encontrada.");
            pausar();
            return;
        }
        if (alertaEncontrada.isResuelta()) {
            System.out.println("  Esta alerta ya estaba resuelta.");
            pausar();
            return;
        }

        alertaEncontrada.setResuelta(true);
        System.out.println("  Alerta marcada como resuelta.");
        pausar();
    }

    // Genera una orden de reposicion con todos los medicamentos que tienen alerta
    // de stock minimo activa. Pide el doble del stock minimo
    private void generarOrden() {
        ArrayList<Alerta> alertasStock = new ArrayList<>();
        for (Alerta a : sistema.getAlertasActivas()) {
            if (a.getTipo() == TipoAlerta.STOCK_MINIMO) {
                alertasStock.add(a);
            }
        }

        if (alertasStock.isEmpty()) {
            System.out.println("  No hay alertas de stock minimo activas para generar una orden.");
            pausar();
            return;
        }

        // Creamos las lineas de la orden con el doble del stock minimo de cada medicamento
        ArrayList<LineaOrden> lineas = new ArrayList<>();
        for (Alerta a : alertasStock) {
            Medicamento med = a.getMedicamento();
            int cantidadAPedir = med.getStockMinimo() * 2;
            lineas.add(new LineaOrden(med, cantidadAPedir));
        }

        OrdenReposicion orden = new OrdenReposicion(0, LocalDate.now(), lineas);
        sistema.agregarOrden(orden);

        System.out.println("  Orden de reposicion creada con ID: " + orden.getId());
        System.out.println("  Medicamentos incluidos:");
        for (LineaOrden l : lineas) {
            System.out.println("" + l);
        }
        pausar();
    }

    private void verOrdenes() {
        ArrayList<OrdenReposicion> ordenes = sistema.getOrdenes();
        System.out.println();
        System.out.println("--- ORDENES DE REPOSICION (" + ordenes.size() + ") ---");
        if (ordenes.isEmpty()) {
            System.out.println("  No hay ordenes registradas.");
        } else {
            for (OrdenReposicion o : ordenes) {
                System.out.println("  " + o);
            }
        }
        pausar();
    }

    private void aprobarOrden() {
        System.out.print("  ID de la orden a aprobar: ");
        int id = leerEnteroPositivo();

        OrdenReposicion orden = sistema.buscarOrdenPorId(id);
        if (orden == null) {
            System.out.println("  Orden no encontrada.");
            pausar();
            return;
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            System.out.println("  Solo se pueden aprobar ordenes en estado PENDIENTE.");
            pausar();
            return;
        }

        // Cuando se aprueba la orden, incrementamos el stock de cada medicamento
        for (LineaOrden linea : orden.getLineas()) {
            Medicamento m = sistema.buscarMedicamentoPorId(linea.getMedicamento().getId());
            if (m != null) {
                m.setStock(m.getStock() + linea.getCantidadSolicitada());
                System.out.println("  Stock actualizado: " + m.getNombre()
                        + " -> " + m.getStock() + " unidades");
            }
        }

        orden.setEstado(EstadoOrden.APROBADA);
        System.out.println("  Orden aprobada y stock actualizado.");
        pausar();
    }

    private void cancelarOrden() {
        System.out.print("  ID de la orden a cancelar: ");
        int id = leerEnteroPositivo();

        OrdenReposicion orden = sistema.buscarOrdenPorId(id);
        if (orden == null) {
            System.out.println("  Orden no encontrada.");
            pausar();
            return;
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            System.out.println("  Solo se pueden cancelar ordenes en estado PENDIENTE.");
            pausar();
            return;
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        System.out.println("  Orden cancelada.");
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

    private void pausar() {
        System.out.print("\n  Pulsa Enter para continuar...");
        scanner.nextLine();
    }
}

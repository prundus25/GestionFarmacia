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

/**
 * Menú de gestión de alertas y órdenes de reposición.
 * Permite ver alertas activas o históricas, marcarlas
 * como resueltas, generar órdenes de reposición a
 * partir de alertas de stock mínimo activas, confirmar
 * la recepción de las reposiciones o cancelarlas.
 */
public class MenuAlertas {

    private SistemaFarmacia sistema;

    /**
     * Crea un nuevo MenuAlertas asociado al
     * sistema de farmacia dado.
     *
     * @param sistema instancia central del sistema
     */
    public MenuAlertas(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    /**
     * Lanza el bucle interactivo del menú de alertas.
     * Continúa mostrando opciones hasta que el usuario
     * elige la opción 0 (Volver).
     */
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
            System.out.println("  6. Confirmar recepcion de reposicion");
            System.out.println("  7. Cancelar orden de reposicion");
            System.out.println("  8. Retirar stock de medicamento caducado");
            System.out.println("  0. Volver");
            System.out.println("==================================");
            System.out.print("  Opcion: ");

            int opcion = Consola.leerEntero(0, 8);

            switch (opcion) {
                case 1 -> verAlertasActivas();
                case 2 -> verTodasLasAlertas();
                case 3 -> resolverAlerta();
                case 4 -> generarOrden();
                case 5 -> verOrdenes();
                case 6 -> confirmarRecepcion();
                case 7 -> cancelarOrden();
                case 8 -> retirarStockCaducado();
                case 0 -> salir = true;
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
        Consola.pausar();
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
        Consola.pausar();
    }

    private void resolverAlerta() {
        System.out.print("  ID de la alerta a resolver: ");
        int id = Consola.leerEnteroPositivo();

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
            Consola.pausar();
            return;
        }
        if (alertaEncontrada.isResuelta()) {
            System.out.println("  Esta alerta ya estaba resuelta.");
            Consola.pausar();
            return;
        }

        alertaEncontrada.setResuelta(true);
        System.out.println("  Alerta marcada como resuelta.");
        Consola.pausar();
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
            Consola.pausar();
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
        Consola.pausar();
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
        Consola.pausar();
    }

    private ArrayList<OrdenReposicion> verOrdenesPendientes() {
        ArrayList<OrdenReposicion> pendientes = new ArrayList<>();
        for (OrdenReposicion o : sistema.getOrdenes()) {
            if (o.getEstado() == EstadoOrden.PENDIENTE) {
                pendientes.add(o);
            }
        }
        System.out.println();
        System.out.println("--- ORDENES PENDIENTES ---");
        if (pendientes.isEmpty()) {
            System.out.println("  No hay ordenes pendientes de recepcion.");
        } else {
            for (OrdenReposicion o : pendientes) {
                System.out.println("  " + o);
            }
        }
        return pendientes;
    }

    private void confirmarRecepcion() {
        ArrayList<OrdenReposicion> pendientes = verOrdenesPendientes();
        if (pendientes.isEmpty()) {
            Consola.pausar();
            return;
        }

        System.out.print("  ID de la orden recibida: ");
        int id = Consola.leerEnteroPositivo();

        OrdenReposicion orden = sistema.buscarOrdenPorId(id);
        if (orden == null) {
            System.out.println("  Orden no encontrada.");
            Consola.pausar();
            return;
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            System.out.println("  Solo se pueden confirmar ordenes en estado PENDIENTE.");
            Consola.pausar();
            return;
        }

        // Al confirmar la recepcion, se incrementa el stock de cada medicamento recibido
        for (LineaOrden linea : orden.getLineas()) {
            Medicamento m = sistema.buscarMedicamentoPorId(linea.getMedicamento().getId());
            if (m != null) {
                m.setStock(m.getStock() + linea.getCantidadSolicitada());
                System.out.println("  Stock actualizado: " + m.getNombre()
                        + " -> " + m.getStock() + " unidades");

                // Si el nuevo stock supera el minimo, resolver la alerta STOCK_MINIMO activa
                if (m.getStock() > m.getStockMinimo()) {
                    for (Alerta a : sistema.getAlertasActivas()) {
                        if (a.getMedicamento().getId() == m.getId()
                                && a.getTipo() == TipoAlerta.STOCK_MINIMO) {
                            a.setResuelta(true);
                            System.out.println("  [AUTO] Alerta de stock minimo resuelta para: "
                                    + m.getNombre());
                            break;
                        }
                    }
                }
            }
        }

        orden.setEstado(EstadoOrden.APROBADA);
        System.out.println("  Recepcion confirmada. Stock actualizado.");
        Consola.pausar();
    }

    private void cancelarOrden() {
        System.out.print("  ID de la orden a cancelar: ");
        int id = Consola.leerEnteroPositivo();

        OrdenReposicion orden = sistema.buscarOrdenPorId(id);
        if (orden == null) {
            System.out.println("  Orden no encontrada.");
            Consola.pausar();
            return;
        }
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            System.out.println("  Solo se pueden cancelar ordenes en estado PENDIENTE.");
            Consola.pausar();
            return;
        }

        orden.setEstado(EstadoOrden.CANCELADA);
        System.out.println("  Orden cancelada.");
        Consola.pausar();
    }

    private void retirarStockCaducado() {
        // Mostrar solo alertas de caducidad activas
        ArrayList<Alerta> alertasCad = new ArrayList<>();
        for (Alerta a : sistema.getAlertasActivas()) {
            if (a.getTipo() == TipoAlerta.CADUCIDAD) {
                alertasCad.add(a);
            }
        }

        if (alertasCad.isEmpty()) {
            System.out.println("  No hay alertas de caducidad activas.");
            Consola.pausar();
            return;
        }

        System.out.println();
        System.out.println("--- ALERTAS DE CADUCIDAD ACTIVAS ---");
        for (Alerta a : alertasCad) {
            System.out.println("  " + a);
        }

        System.out.print("  ID de la alerta cuyo medicamento retirar: ");
        int idAlerta = Consola.leerEnteroPositivo();

        Alerta alerta = null;
        for (Alerta a : alertasCad) {
            if (a.getId() == idAlerta) {
                alerta = a;
                break;
            }
        }

        if (alerta == null) {
            System.out.println("  Alerta no encontrada o no es de caducidad.");
            Consola.pausar();
            return;
        }

        Medicamento med = sistema.buscarMedicamentoPorId(alerta.getMedicamento().getId());
        if (med == null) {
            System.out.println("  Medicamento no encontrado en el inventario.");
            Consola.pausar();
            return;
        }

        int stockAnterior = med.getStock();
        med.setStock(0);
        alerta.setResuelta(true);
        System.out.println("  Stock de '" + med.getNombre() + "' puesto a 0"
                + " (habia " + stockAnterior + " unidades). Alerta resuelta.");

        // Si habia stock, generar alerta de stock minimo para que se pueda reponer
        if (stockAnterior > 0
                && !sistema.existeAlertaActiva(med.getId(), TipoAlerta.STOCK_MINIMO)) {
            Alerta nuevaAlerta = new Alerta(0, TipoAlerta.STOCK_MINIMO, med, LocalDate.now());
            sistema.agregarAlerta(nuevaAlerta);
            System.out.println("  [ALERTA] Se ha generado una alerta de stock minimo.");
            System.out.println("  Use la opcion 4 para generar una orden de reposicion.");
        }

        Consola.pausar();
    }
}

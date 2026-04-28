package ui;

import model.*;
import model.enums.EstadoReceta;
import model.enums.TipoAlerta;
import sistema.SistemaFarmacia;

import java.time.LocalDate;
import java.util.ArrayList;

public class MenuRecetas {

    private SistemaFarmacia sistema;

    public MenuRecetas(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== GESTION DE RECETAS =====");
            System.out.println("  1. Listar todas las recetas");
            System.out.println("  2. Recetas pendientes de dispensar");
            System.out.println("  3. Recetas pendientes de autorizacion");
            System.out.println("  4. Historial por paciente");
            System.out.println("  5. Nueva receta");
            System.out.println("  6. Dispensar receta");
            System.out.println("  7. Autorizar receta (comité)");
            System.out.println("  8. Cancelar receta");
            System.out.println("  9. Ver detalle de receta");
            System.out.println("  0. Volver");
            System.out.println("==============================");
            System.out.print("  Opcion: ");

            int opcion = Consola.leerEntero(0, 9);

            switch (opcion){
                case 1 -> listarTodas();
                case 2 -> listarPendientes();
                case 3 -> listarPendientesAutorizacion();
                case 4 -> historialPorPaciente();
                case 5 -> nuevaReceta();
                case 6 -> dispensarReceta();
                case 7 -> autorizarReceta();
                case 8 -> cancelarReceta();
                case 9 -> verDetalle();
                case 0 -> salir = true;
            }
        }
    }

    private void listarTodas() {
        ArrayList<Receta> recetas = sistema.getRecetas();
        System.out.println();
        System.out.println("--- TODAS LAS RECETAS (" + recetas.size() + ") ---");
        if (recetas.isEmpty()) {
            System.out.println("  No hay recetas registradas.");
        } else {
            for (Receta r : recetas) {
                System.out.println("  " + r);
            }
        }
        Consola.pausar();
    }

    private void listarPendientes() {
        System.out.println();
        System.out.println("--- RECETAS PENDIENTES ---");
        boolean hayAlguna = false;
        for (Receta r : sistema.getRecetas()) {
            if (r.getEstado() == EstadoReceta.PENDIENTE) {
                System.out.println("  " + r);
                hayAlguna = true;
            }
        }
        if (!hayAlguna) {
            System.out.println("  No hay recetas pendientes.");
        }
        Consola.pausar();
    }

    private void listarPendientesAutorizacion() {
        System.out.println();
        System.out.println("--- PENDIENTES DE AUTORIZACION ---");
        boolean hayAlguna = false;
        for (Receta r : sistema.getRecetas()) {
            if (r.getEstado() == EstadoReceta.PENDIENTE_AUTORIZACION) {
                System.out.println("  " + r);
                hayAlguna = true;
            }
        }
        if (!hayAlguna) {
            System.out.println("  No hay recetas pendientes de autorizacion.");
        }
        Consola.pausar();
    }

    private void historialPorPaciente() {
        System.out.print("  ID del paciente: ");
        int idPaciente = Consola.leerEnteroPositivo();
        Paciente paciente = sistema.buscarPacientePorId(idPaciente);
        if (paciente == null) {
            System.out.println("  Paciente no encontrado.");
            Consola.pausar();
            return;
        }

        System.out.println();
        System.out.println("--- RECETAS DE " + paciente.getNombreCompleto() + " ---");
        boolean hayAlguna = false;
        for (Receta r : sistema.getRecetas()) {
            if (r.getPaciente().getId() == idPaciente) {
                System.out.println("  " + r);
                hayAlguna = true;
            }
        }
        if (!hayAlguna) {
            System.out.println("  No hay recetas para este paciente.");
        }
        Consola.pausar();
    }

    private void nuevaReceta() {
        System.out.println();
        System.out.println("--- NUEVA RECETA ---");

        // Seleccionar paciente
        System.out.print("  ID del paciente: ");
        int idPaciente = Consola.leerEnteroPositivo();
        Paciente paciente = sistema.buscarPacientePorId(idPaciente);
        if (paciente == null) {
            System.out.println("  Paciente no encontrado.");
            Consola.pausar();
            return;
        }

        // Mostrar alergias del paciente para que el farmacéutico las tenga en cuenta
        if (!paciente.getAlergias().isEmpty()) {
            System.out.println("  [AVISO] Este paciente tiene alergias registradas: "
                    + String.join(", ", paciente.getAlergias()));
        }

        // Seleccionar médico
        System.out.print("  ID del medico: ");
        int idMedico = Consola.leerEnteroPositivo();
        Medico medico = sistema.buscarMedicoPorId(idMedico);
        if (medico == null) {
            System.out.println("  Medico no encontrado.");
            Consola.pausar();
            return;
        }

        // Añadir los medicamentos de la receta
        ArrayList<LineaReceta> lineas = new ArrayList<>();
        boolean seguirAñadiendo = true;

        while (seguirAñadiendo) {
            System.out.print("  ID del medicamento (0 para terminar): ");
            int idMed = Consola.leerEnteroPositivo();
            if (idMed == 0) {
                seguirAñadiendo = false;
                continue;
            }

            Medicamento med = sistema.buscarMedicamentoPorId(idMed);
            if (med == null) {
                System.out.println("  Medicamento no encontrado, intentalo de nuevo.");
                continue;
            }

            System.out.print("  Dosis (ej. 500mg): ");
            String dosis = Consola.scanner.nextLine().trim();

            System.out.print("  Frecuencia (ej. cada 8 horas): ");
            String frecuencia = Consola.scanner.nextLine().trim();

            System.out.print("  Duracion en dias: ");
            int dias = Consola.leerEnteroPositivo();

            System.out.print("  Cantidad de unidades: ");
            int cantidad = Consola.leerEnteroPositivo();

            lineas.add(new LineaReceta(med, dosis, frecuencia, dias, cantidad));
            System.out.println("  Añadido: " + med.getNombre());
        }

        if (lineas.isEmpty()) {
            System.out.println("  No se añadio ningun medicamento, receta cancelada.");
            Consola.pausar();
            return;
        }

        System.out.print("  ¿Es receta cronica? (s/n): ");
        boolean cronica = Consola.scanner.nextLine().trim().equalsIgnoreCase("s");

        // Aviso genérico de interacciones
        System.out.println("  [AVISO] Recuerde verificar manualmente posibles interacciones");
        System.out.println("  entre los medicamentos prescritos.");

        // Crear la receta
        Receta receta = new Receta(0, paciente, medico, lineas, LocalDate.now(), cronica);

        // Si hay medicamentos restringidos la receta necesita autorización del comité
        boolean tieneRestringidos = false;
        for (LineaReceta l : lineas) {
            if (l.getMedicamento().isRestringido()) {
                tieneRestringidos = true;
                break;
            }
        }

        if (tieneRestringidos) {
            receta.setEstado(EstadoReceta.PENDIENTE_AUTORIZACION);
            System.out.println("  [AUTORIZACION REQUERIDA] La receta contiene medicamentos");
            System.out.println("  de uso restringido. Se ha notificado al comite farmacoterapeutico.");
        }

        sistema.agregarReceta(receta);
        System.out.println("  Receta creada con ID: " + receta.getId());
        Consola.pausar();
    }

    private void dispensarReceta() {
        System.out.print("  ID de la receta a dispensar: ");
        int id = Consola.leerEnteroPositivo();

        Receta receta = sistema.buscarRecetaPorId(id);
        if (receta == null) {
            System.out.println("  Receta no encontrada.");
            Consola.pausar();
            return;
        }

        // Comprobamos que la receta se puede dispensar
        if (receta.getEstado() == EstadoReceta.DISPENSADA) {
            System.out.println("  Esta receta ya fue dispensada.");
            Consola.pausar();
            return;
        }
        if (receta.getEstado() == EstadoReceta.CANCELADA) {
            System.out.println("  Esta receta esta cancelada.");
            Consola.pausar();
            return;
        }
        if (receta.getEstado() == EstadoReceta.PENDIENTE_AUTORIZACION) {
            System.out.println("  Esta receta esta pendiente de autorizacion del comite.");
            Consola.pausar();
            return;
        }

        // Primero comprobamos que hay stock suficiente para todos los medicamentos
        for (LineaReceta linea : receta.getLineas()) {
            Medicamento m = sistema.buscarMedicamentoPorId(linea.getMedicamento().getId());
            if (m == null) {
                System.out.println("  Error: medicamento no encontrado en el inventario.");
                Consola.pausar();
                return;
            }
            if (m.getStock() < linea.getCantidad()) {
                System.out.println("  Stock insuficiente para: " + m.getNombre()
                        + " (disponible: " + m.getStock()
                        + ", necesario: " + linea.getCantidad() + ")");
                Consola.pausar();
                return;
            }
        }

        // Descontamos el stock de cada medicamento
        for (LineaReceta linea : receta.getLineas()) {
            Medicamento m = sistema.buscarMedicamentoPorId(linea.getMedicamento().getId());
            m.setStock(m.getStock() - linea.getCantidad());

            // Si el stock baja del mínimo generamos una alerta
            if (m.getStock() <= m.getStockMinimo()) {
                if (!sistema.existeAlertaActiva(m.getId(), TipoAlerta.STOCK_MINIMO)) {
                    Alerta alerta = new Alerta(0, TipoAlerta.STOCK_MINIMO, m, LocalDate.now());
                    sistema.agregarAlerta(alerta);
                    System.out.println("  [ALERTA] Stock minimo alcanzado para: " + m.getNombre());
                }
            }
        }

        receta.setEstado(EstadoReceta.DISPENSADA);
        System.out.println("  Receta dispensada correctamente.");
        System.out.println("  [FACTURACION] Los cargos han sido registrados en el sistema.");
        if (receta.isCronica()) {
            System.out.println("  [CRONICA] Se ha programado la reposicion automatica del tratamiento.");
        }
        Consola.pausar();
    }

    private void autorizarReceta() {
        System.out.print("  ID de la receta a autorizar: ");
        int id = Consola.leerEnteroPositivo();

        Receta receta = sistema.buscarRecetaPorId(id);
        if (receta == null) {
            System.out.println("  Receta no encontrada.");
            Consola.pausar();
            return;
        }
        if (receta.getEstado() != EstadoReceta.PENDIENTE_AUTORIZACION) {
            System.out.println("  Esta receta no esta pendiente de autorizacion.");
            Consola.pausar();
            return;
        }

        receta.setEstado(EstadoReceta.PENDIENTE);
        System.out.println("  Receta autorizada. Ya puede ser dispensada.");
        Consola.pausar();
    }

    private void cancelarReceta() {
        System.out.print("  ID de la receta a cancelar: ");
        int id = Consola.leerEnteroPositivo();

        Receta receta = sistema.buscarRecetaPorId(id);
        if (receta == null) {
            System.out.println("  Receta no encontrada.");
            Consola.pausar();
            return;
        }
        if (receta.getEstado() == EstadoReceta.CANCELADA) {
            System.out.println("  La receta ya esta cancelada.");
            Consola.pausar();
            return;
        }
        if (receta.getEstado() == EstadoReceta.DISPENSADA) {
            System.out.println("  No se puede cancelar una receta ya dispensada.");
            Consola.pausar();
            return;
        }

        receta.setEstado(EstadoReceta.CANCELADA);
        System.out.println("  Receta cancelada.");
        Consola.pausar();
    }

    private void verDetalle() {
        System.out.print("  ID de la receta: ");
        int id = Consola.leerEnteroPositivo();

        Receta receta = sistema.buscarRecetaPorId(id);
        if (receta == null) {
            System.out.println("  Receta no encontrada.");
            Consola.pausar();
            return;
        }

        System.out.println();
        System.out.println("--- DETALLE DE RECETA #" + receta.getId() + " ---");
        System.out.println("  Paciente:  " + receta.getPaciente().getNombreCompleto());
        System.out.println("  Medico:    Dr/a. " + receta.getMedico().getNombreCompleto());
        System.out.println("  Fecha:     " + receta.getFecha());
        System.out.println("  Estado:    " + receta.getEstado());
        System.out.println("  Cronica:   " + (receta.isCronica() ? "Si" : "No"));
        System.out.println("  Medicamentos:");
        for (LineaReceta l : receta.getLineas()) {
            System.out.println("" + l);
        }
        Consola.pausar();
    }

}

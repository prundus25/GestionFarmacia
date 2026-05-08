package ui;

import model.*;
import model.enums.EstadoReceta;
import model.enums.TipoAlerta;
import sistema.SistemaFarmacia;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Menú de gestión de recetas médicas.
 *
 * Permite al farmacéutico listar, validar, reservar stock
 * y dispensar recetas. También ofrece la comprobación
 * semanal de reposiciones para recetas crónicas.
 *
 * NOTA DE DISEÑO: Los estados PENDIENTE_AUTORIZACION y PENDIENTE_MEDICO
 * son gestionados por el comité farmacoterapéutico y el médico
 * respectivamente, que son actores externos a este módulo de farmacia.
 * Por tanto, no se expone ninguna opción para sacar una receta de esos
 * estados: esa transición se produce en otra parte del sistema a la que
 * el personal de farmacia no tiene acceso. Desde este menú solo se
 * actúa sobre recetas en PENDIENTE_VALIDAR (validar y reservar stock)
 * y STOCK_RESERVADO (dispensar).
 */
public class MenuRecetas {

    private SistemaFarmacia sistema;

    /**
     * Crea un nuevo MenuRecetas asociado al sistema de farmacia dado.
     *
     * @param sistema instancia central del sistema
     */
    public MenuRecetas(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    /**
     * Lanza el bucle interactivo del menú de recetas.
     */
    public void mostrar() {
        boolean salir = false;
        while (!salir) {
            System.out.println();
            System.out.println("===== GESTION DE RECETAS =====");
            System.out.println("  1. Listar todas las recetas");
            System.out.println("  2. Recetas pendientes de validar");
            System.out.println("  3. Recetas con stock reservado");
            System.out.println("  4. Ver detalle de receta");
            System.out.println("  5. Validar y reservar stock");
            System.out.println("  6. Dispensar receta");
            System.out.println("  7. Comprobacion semanal de cronicos");
            System.out.println("  8. Recetas por paciente");
            System.out.println("  0. Volver");
            System.out.println("==============================");
            System.out.print("  Opcion: ");

            int opcion = Consola.leerEntero(0, 8);

            switch (opcion) {
                case  1 -> listarTodas();
                case  2 -> listarPorEstado(EstadoReceta.PENDIENTE_VALIDAR, "PENDIENTES DE VALIDAR");
                case  3 -> listarPorEstado(EstadoReceta.STOCK_RESERVADO, "CON STOCK RESERVADO");
                case  4 -> verDetalle();
                case  5 -> validarYReservarStock();
                case  6 -> dispensarReceta();
                case  7 -> comprobacionSemanal();
                case  8 -> recetasPorPaciente();
                case  0 -> salir = true;
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

    private void listarPorEstado(EstadoReceta estado, String titulo) {
        System.out.println();
        System.out.println("--- " + titulo + " ---");
        boolean hayAlguna = false;
        for (Receta r : sistema.getRecetas()) {
            if (r.getEstado() == estado) {
                System.out.println("  " + r);
                hayAlguna = true;
            }
        }
        if (!hayAlguna) {
            System.out.println("  No hay recetas en este estado.");
        }
        Consola.pausar();
    }

    private void recetasPorPaciente() {
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
            if (r.getPaciente() == paciente) {
                System.out.println("  " + r);
                hayAlguna = true;
            }
        }
        if (!hayAlguna) {
            System.out.println("  No hay recetas para este paciente.");
        }
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
        if (receta.getFechaDispensacion() != null) {
            System.out.println("  Dispensada: " + receta.getFechaDispensacion());
        }
        System.out.println("  Medicamentos:");
        for (LineaReceta l : receta.getLineas()) {
            System.out.println("" + l);
            System.out.println("    (stock disponible: "
                    + sistema.calcularStockDisponible(l.getMedicamento().getId()) + " caja(s))");
            if (receta.isCronica() && receta.getEstado() == EstadoReceta.DISPENSADA) {
                System.out.println("    Prox. reposicion: " + l.getFechaProximaReposicion());
            }
        }
        Consola.pausar();
    }

    /**
     * Valida una receta en estado PENDIENTE_VALIDAR y la avanza al estado correcto:
     * PENDIENTE_AUTORIZACION si contiene medicamentos restringidos,
     * PENDIENTE_MEDICO si hay interacciones o dosis excesiva,
     * STOCK_RESERVADO si todo es correcto y hay stock,
     * o permanece en PENDIENTE_VALIDAR si falta stock.
     */
    private void validarYReservarStock() {
        System.out.print("  ID de la receta a validar: ");
        int id = Consola.leerEnteroPositivo();

        Receta receta = sistema.buscarRecetaPorId(id);
        if (receta == null) {
            System.out.println("  Receta no encontrada.");
            Consola.pausar();
            return;
        }
        if (receta.getEstado() != EstadoReceta.PENDIENTE_VALIDAR) {
            System.out.println("  Solo se pueden validar recetas en estado PENDIENTE_VALIDAR.");
            System.out.println("  Estado actual: " + receta.getEstado());
            Consola.pausar();
            return;
        }

        // 1. Comprobar medicamentos restringidos
        boolean tieneRestringidos = receta.getLineas().stream()
                .anyMatch(l -> l.getMedicamento().isRestringido());
        if (tieneRestringidos) {
            receta.setEstado(EstadoReceta.PENDIENTE_AUTORIZACION);
            System.out.println("  Receta contiene medicamentos restringidos.");
            System.out.println("  Pasa a PENDIENTE_AUTORIZACION. Debe ser autorizada por el comite farmacoterapeutico.");
            Consola.pausar();
            return;
        }

        // 2. Comprobar interacciones peligrosas
        if (sistema.tieneInteraccionesPeligrosas(receta)) {
            System.out.println("  [AVISO] Interacciones peligrosas detectadas:");
            List<LineaReceta> lineas = receta.getLineas();
            for (int i = 0; i < lineas.size(); i++) {
                Medicamento m = lineas.get(i).getMedicamento();
                for (int j = i + 1; j < lineas.size(); j++) {
                    int otroId = lineas.get(j).getMedicamento().getId();
                    if (m.getInteraccionesPeligrosas().contains(otroId)) {
                        System.out.println("    - " + m.getNombre()
                                + " interacciona con " + lineas.get(j).getMedicamento().getNombre());
                    }
                }
            }
            receta.setEstado(EstadoReceta.PENDIENTE_MEDICO);
            System.out.println("  Receta marcada como PENDIENTE_MEDICO. Medico informado.");
            Consola.pausar();
            return;
        }

        // 3. Comprobar dosis excesiva
        if (sistema.tieneDosisExcesiva(receta)) {
            System.out.println("  [AVISO] Dosis excesiva detectada:");
            for (LineaReceta l : receta.getLineas()) {
                if (l.getDosisMg() > l.getMedicamento().getDosisMaximaMg()) {
                    System.out.println("    - " + l.getMedicamento().getNombre()
                            + ": prescrita " + l.getDosisMg() + "mg, maximo "
                            + l.getMedicamento().getDosisMaximaMg() + "mg");
                }
            }
            receta.setEstado(EstadoReceta.PENDIENTE_MEDICO);
            System.out.println("  Receta marcada como PENDIENTE_MEDICO. Medico informado.");
            Consola.pausar();
            return;
        }

        // 4. Comprobar stock disponible para cada línea
        boolean hayStockParaTodo = true;
        boolean hayAlternativas = false;
        for (LineaReceta l : receta.getLineas()) {
            int disponible = sistema.calcularStockDisponible(l.getMedicamento().getId());
            if (disponible < l.getCajas()) {
                hayStockParaTodo = false;
                System.out.println("  [STOCK INSUFICIENTE] " + l.getMedicamento().getNombre()
                        + ": necesarias " + l.getCajas() + " caja(s), disponibles " + disponible);
                if (comprobacionAlternativas(l.getMedicamento())) {
                    hayAlternativas = true;
                }
            }
        }

        if (hayStockParaTodo) {
            receta.setEstado(EstadoReceta.STOCK_RESERVADO);
            System.out.println("  Receta validada correctamente. Pasa a STOCK_RESERVADO.");
            System.out.println("  Las cajas quedan comprometidas y se descuentan del stock disponible.");
        } else if (hayAlternativas) {
            receta.setEstado(EstadoReceta.PENDIENTE_MEDICO);
            System.out.println("  Receta marcada como PENDIENTE_MEDICO. Medico informado sobre alternativas disponibles.");
        } else {
            receta.setEstado(EstadoReceta.PENDIENTE_STOCK);
            generarOrdenesReposicion(receta);
            System.out.println("  Receta marcada como PENDIENTE_STOCK. Orden de reposicion generada.");
            System.out.println("  Cuando llegue el stock se avanzara automaticamente a STOCK_RESERVADO.");
        }
        Consola.pausar();
    }

    /**
     * Genera una orden de reposicion por cada linea de la receta que no tenga
     * stock suficiente y cuyo medicamento no tenga alternativas con stock.
     *
     * @param receta receta con lineas sin stock disponible
     */
    private void generarOrdenesReposicion(Receta receta) {
        for (LineaReceta l : receta.getLineas()) {
            int disponible = sistema.calcularStockDisponible(l.getMedicamento().getId());
            if (disponible < l.getCajas()) {
                int falta = l.getCajas() - disponible;
                ArrayList<LineaOrden> lineasOrden = new ArrayList<>();
                lineasOrden.add(new LineaOrden(l.getMedicamento(), falta));
                OrdenReposicion orden = new OrdenReposicion(0, LocalDate.now(), lineasOrden);
                sistema.agregarOrden(orden);
                System.out.println("  [ORDEN #" + orden.getId() + "] Reposicion generada para "
                        + l.getMedicamento().getNombre() + " (" + falta + " caja(s)).");
            }
        }
    }

    /**
     * Comprueba si alguna alternativa del medicamento tiene stock disponible
     * y muestra las que sí lo tienen.
     *
     * @param medicamento medicamento sin stock suficiente
     * @return true si existe al menos una alternativa con stock disponible
     */
    private boolean comprobacionAlternativas(Medicamento medicamento) {
        List<Integer> alts = medicamento.getAlternativas();
        if (alts.isEmpty()) {
            return false;
        }
        boolean hayAlguna = alts.stream()
                .anyMatch(altId -> {
                    Medicamento alt = sistema.buscarMedicamentoPorId(altId);
                    return alt != null && sistema.calcularStockDisponible(alt.getId()) > 0;
                });
        if (!hayAlguna) {
            return false;
        }
        System.out.println("    Alternativas disponibles (se ha informado al medico):");
        for (int altId : alts) {
            Medicamento alt = sistema.buscarMedicamentoPorId(altId);
            if (alt != null && sistema.calcularStockDisponible(alt.getId()) > 0) {
                System.out.println("      [" + alt.getId() + "] " + alt.getNombre() + " - stock disponible: "
                        + sistema.calcularStockDisponible(alt.getId()) + " caja(s)");
            }
        }
        return true;
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
        if (receta.getEstado() != EstadoReceta.STOCK_RESERVADO) {
            System.out.println("  Solo se pueden dispensar recetas en estado STOCK_RESERVADO.");
            System.out.println("  Estado actual: " + receta.getEstado());
            Consola.pausar();
            return;
        }

        // Descontar stock real
        for (LineaReceta linea : receta.getLineas()) {
            Medicamento m = sistema.buscarMedicamentoPorId(linea.getMedicamento().getId());
            m.setStock(m.getStock() - linea.getCajas());

            if (m.getStock() <= m.getStockMinimo()) {
                if (!sistema.existeAlertaActiva(m.getId(), TipoAlerta.STOCK_MINIMO)) {
                    Alerta alerta = new Alerta(0, TipoAlerta.STOCK_MINIMO, m, LocalDate.now());
                    sistema.agregarAlerta(alerta);
                    System.out.println("  [ALERTA] Stock minimo alcanzado para: " + m.getNombre());
                }
            }
        }

        receta.setEstado(EstadoReceta.DISPENSADA);
        receta.setFechaDispensacion(LocalDate.now());

        if (receta.isCronica()) {
            for (LineaReceta linea : receta.getLineas()) {
                linea.setFechaProximaReposicion(LocalDate.now().plusDays(linea.getFrecuenciaDias()));
                System.out.println("  [" + linea.getMedicamento().getNombre() + "] "
                        + "Proxima reposicion: " + linea.getFechaProximaReposicion());
            }
        }

        System.out.println("  Receta dispensada correctamente.");
        Consola.pausar();
    }

    private void comprobacionSemanal() {
        System.out.println();
        System.out.println("--- COMPROBACION SEMANAL DE CRONICOS ---");
        int renovadas = sistema.comprobarReposicionCronicos();
        if (renovadas == 0) {
            System.out.println("  No hay recetas cronicas que requieran reposicion esta semana.");
        } else {
            System.out.println("  Renovaciones procesadas: " + renovadas);
        }
        Consola.pausar();
    }
}

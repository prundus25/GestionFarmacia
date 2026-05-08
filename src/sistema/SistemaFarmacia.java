package sistema;

import model.*;
import model.enums.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Núcleo de datos del sistema de farmacia hospitalaria.
 * 
 * Almacena las listas de todas las entidades del dominio
 * (medicamentos, pacientes, médicos, recetas, alertas y
 * órdenes de reposición) y expone los métodos CRUD
 * necesarios para gestionarlas. Gestiona también la
 * persistencia mediante serialización Java al fichero
 * data/farmacia.dat y la carga inicial de datos
 * de muestra al detectar una primera ejecución.
 */
public class SistemaFarmacia implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Medicamento> medicamentos;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Receta> recetas;
    private ArrayList<Alerta> alertas;
    private ArrayList<OrdenReposicion> ordenes;
    private int nextIdMedicamento = 1;
    private int nextIdAlerta = 1;
    private int nextIdOrden = 1;

    /**
     * Crea un nuevo SistemaFarmacia con todas las
     * listas vacías.
     */
    public SistemaFarmacia() {
        medicamentos = new ArrayList<>();
        pacientes = new ArrayList<>();
        medicos = new ArrayList<>();
        recetas = new ArrayList<>();
        alertas = new ArrayList<>();
        ordenes = new ArrayList<>();
    }

    // MEDICAMENTOS 

    /**
     * Añade un medicamento al inventario asignándole un
     * identificador único.
     *
     * @param m medicamento a registrar; su id
     *          se sobreescribe con el próximo disponible
     */
    public void agregarMedicamento(Medicamento m) {
        m.setId(nextIdMedicamento);
        nextIdMedicamento++;
        medicamentos.add(m);
    }

    /**
     * Devuelve la lista completa de medicamentos del
     * inventario.
     *
     * @return lista de medicamentos
     */
    public ArrayList<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    /**
     * Busca un medicamento por su identificador único.
     *
     * @param id identificador del medicamento
     * @return el medicamento encontrado, o null
     *         si no existe ninguno con ese id
     */
    public Medicamento buscarMedicamentoPorId(int id) {
        for (Medicamento m : medicamentos) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null; // no encontrado
    }

    /**
     * Elimina el medicamento con el identificador dado.
     *
     * @param id identificador del medicamento a eliminar
     * @return true si se ha eliminado;
     *         false si no se encontró
     */
    public boolean eliminarMedicamento(int id) {
        Medicamento m = buscarMedicamentoPorId(id);
        if (m != null) {
            // Eliminar este ID de las listas de alternativas del resto
            for (Medicamento otro : medicamentos) {
                otro.getAlternativas().remove(Integer.valueOf(id));
            }
            medicamentos.remove(m);
            return true;
        }
        return false;
    }

    /**
     * Registra una relación de alternativa entre dos
     * medicamentos en ambos sentidos.
     *
     * @param idA identificador del primer medicamento
     * @param idB identificador del segundo medicamento
     */
    public void añadirAlternativa(int idA, int idB) {
        Medicamento a = buscarMedicamentoPorId(idA);
        Medicamento b = buscarMedicamentoPorId(idB);
        if (a == null || b == null || idA == idB) return;
        if (!a.getAlternativas().contains(idB)) a.getAlternativas().add(idB);
        if (!b.getAlternativas().contains(idA)) b.getAlternativas().add(idA);
    }

    /**
     * Elimina la relación de alternativa entre dos
     * medicamentos en ambos sentidos.
     *
     * @param idA identificador del primer medicamento
     * @param idB identificador del segundo medicamento
     */
    public void eliminarAlternativa(int idA, int idB) {
        Medicamento a = buscarMedicamentoPorId(idA);
        Medicamento b = buscarMedicamentoPorId(idB);
        if (a != null) a.getAlternativas().remove(Integer.valueOf(idB));
        if (b != null) b.getAlternativas().remove(Integer.valueOf(idA));
    }

    /**
     * Calcula el stock reservado de un medicamento, es decir,
     * la suma de cajas comprometidas en todas las recetas con
     * estado STOCK_RESERVADO.
     *
     * @param idMedicamento identificador del medicamento
     * @return total de cajas reservadas
     */
    public int calcularStockReservado(int idMedicamento) {
        int reservado = 0;
        for (Receta r : recetas) {
            if (r.getEstado() == EstadoReceta.STOCK_RESERVADO) {
                for (LineaReceta l : r.getLineas()) {
                    if (l.getMedicamento().getId() == idMedicamento) {
                        reservado += l.getCajas();
                    }
                }
            }
        }
        return reservado;
    }

    /**
     * Calcula el stock disponible de un medicamento:
     * stock real menos cajas ya reservadas para recetas en
     * estado STOCK_RESERVADO y menos cajas reservadas para
     * el proximo ciclo de recetas cronicas.
     *
     * @param idMedicamento identificador del medicamento
     * @return stock disponible para nuevas reservas
     */
    public int calcularStockDisponible(int idMedicamento) {
        Medicamento m = buscarMedicamentoPorId(idMedicamento);
        if (m == null) return 0;
        return m.getStock()
                - calcularStockReservado(idMedicamento)
                - calcularStockCronicoReservado(idMedicamento);
    }

    /**
     * Calcula el numero de cajas reservadas para el proximo ciclo de
     * recetas cronicas dispensadas, es decir, lineas con el flag
     * proximaCajaReservada activo.
     *
     * @param idMedicamento identificador del medicamento
     * @return numero de cajas comprometidas para proximos ciclos cronicos
     */
    private int calcularStockCronicoReservado(int idMedicamento) {
        int reservado = 0;
        for (Receta r : recetas) {
            if (r.isCronica() && r.getEstado() == EstadoReceta.DISPENSADA) {
                for (LineaReceta l : r.getLineas()) {
                    if (l.getMedicamento().getId() == idMedicamento && l.isProximaCajaReservada()) {
                        reservado++;
                    }
                }
            }
        }
        return reservado;
    }

    /**
     * Comprueba si la receta tiene interacciones peligrosas entre
     * sus medicamentos. Devuelve true si se detecta al menos una.
     *
     * @param receta la receta a comprobar
     * @return true si hay alguna interacción peligrosa
     */
    public boolean tieneInteraccionesPeligrosas(Receta receta) {
        List<LineaReceta> lineas = receta.getLineas();
        for (int i = 0; i < lineas.size(); i++) {
            Medicamento m = lineas.get(i).getMedicamento();
            for (int j = i + 1; j < lineas.size(); j++) {
                int otroId = lineas.get(j).getMedicamento().getId();
                if (m.getInteraccionesPeligrosas().contains(otroId)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Comprueba si alguna línea de la receta supera la dosis máxima
     * permitida del medicamento correspondiente.
     *
     * @param receta la receta a comprobar
     * @return true si al menos una línea tiene dosis excesiva
     */
    public boolean tieneDosisExcesiva(Receta receta) {
        return receta.getLineas().stream().anyMatch(l -> l.getDosisMg() > l.getMedicamento().getDosisMaximaMg());
    }

    /**
     * Realiza la comprobación semanal de recetas crónicas dispensadas.
     *
     * Fase 1 (hasta 7 días antes de fechaProximaReposicion): comprueba el
     * stock disponible para la próxima caja y genera una orden de reposición
     * si no hay suficiente. Solo actúa una vez por ciclo gracias al flag
     * ordenReposicionGenerada. No notifica al paciente.
     *
     * Fase 2 (el día anterior a fechaProximaReposicion): notifica
     * al paciente, descuenta cajasRestantes y avanza fechaProximaReposicion
     * al siguiente ciclo.
     *
     * @return número de ciclos avanzados (fase 2 activada)
     */
    public int comprobarReposicionCronicos() {
        LocalDate hoy = LocalDate.now();
        int renovadas = 0;
        for (Receta r : recetas) {
            if (r.isCronica() && r.getEstado() == EstadoReceta.DISPENSADA) {
                for (LineaReceta l : r.getLineas()) {
                    renovadas += procesarLineaCronica(l, r, hoy);
                }
            }
        }
        return renovadas;
    }

    /**
     * Procesa una linea de receta cronica: ejecuta la fase 1 de comprobacion
     * de stock y la fase 2 de notificacion al paciente si procede.
     *
     * @param l   linea de receta a procesar
     * @param r   receta cronica a la que pertenece la linea
     * @param hoy fecha actual
     * @return 1 si se ha avanzado el ciclo (fase 2 activada), 0 en caso contrario
     */
    private int procesarLineaCronica(LineaReceta l, Receta r, LocalDate hoy) {
        if (l.getCajasRestantes() <= 0) {
            return 0;
        }
        LocalDate proxima = l.getFechaProximaReposicion();
        comprobarStockYGenerarOrden(l, r, hoy, proxima);
        return notificarYAvanzarCiclo(l, r, hoy, proxima) ? 1 : 0;
    }

    /**
     * Fase 1: si la proxima reposicion cae dentro de los proximos 7 dias y
     * aun no se genero orden, comprueba el stock y crea una orden si hace falta.
     * Marca el flag ordenReposicionGenerada para no repetir la accion.
     *
     * @param l       linea de receta
     * @param r       receta a la que pertenece la linea
     * @param hoy     fecha actual
     * @param proxima fecha de proxima reposicion de la linea
     */
    private void comprobarStockYGenerarOrden(LineaReceta l, Receta r, LocalDate hoy, LocalDate proxima) {
        if (proxima.isBefore(hoy.plusDays(8)) && !l.isOrdenReposicionGenerada()) {
            if (calcularStockDisponible(l.getMedicamento().getId()) < 1) {
                ArrayList<LineaOrden> lineasOrden = new ArrayList<>();
                lineasOrden.add(new LineaOrden(l.getMedicamento(), 1));
                agregarOrden(new OrdenReposicion(0, hoy, lineasOrden, r.getId()));
                System.out.println("  [CRONICA] Sin stock para proxima caja de "
                        + l.getMedicamento().getNombre() + " ("
                        + r.getPaciente().getNombreCompleto()
                        + "). Orden de reposicion generada.");
            } else {
                // Stock ya disponible: reservarlo ahora para que no lo tome otra receta
                l.setProximaCajaReservada(true);
            }
            l.setOrdenReposicionGenerada(true);
        }
    }

    /**
     * Fase 2: si hoy es el dia anterior a la proxima reposicion, notifica
     * al paciente, descuenta una caja y avanza la fecha al siguiente ciclo.
     *
     * @param l       linea de receta
     * @param r       receta a la que pertenece la linea
     * @param hoy     fecha actual
     * @param proxima fecha de proxima reposicion de la linea
     * @return true si se ha notificado y avanzado el ciclo
     */
    private boolean notificarYAvanzarCiclo(LineaReceta l, Receta r, LocalDate hoy, LocalDate proxima) {
        if (!proxima.isEqual(hoy.plusDays(1))) {
            return false;
        }
        System.out.println("  [AVISO] Paciente notificado: "
                + r.getPaciente().getNombreCompleto()
                + " - proxima caja de "
                + l.getMedicamento().getNombre() + " disponible.");
        // Descontar la caja del inventario al entregar al paciente
        Medicamento m = buscarMedicamentoPorId(l.getMedicamento().getId());
        if (m != null) {
            m.setStock(m.getStock() - 1);
        }
        l.setCajasRestantes(l.getCajasRestantes() - 1);
        l.setFechaProximaReposicion(proxima.plusDays(l.getDuracionCaja()));
        l.setOrdenReposicionGenerada(false);
        l.setProximaCajaReservada(false);
        return true;
    }

    // PACIENTES

    /**
     * Devuelve la lista completa de pacientes registrados.
     *
     * @return lista de pacientes
     */
    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    /**
     * Busca un paciente por su identificador único.
     *
     * @param id identificador del paciente
     * @return el paciente encontrado, o null
     *         si no existe ninguno con ese id
     */
    public Paciente buscarPacientePorId(int id) {
        for (Paciente p : pacientes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    // MÉDICOS

    /**
     * Devuelve la lista completa de médicos registrados.
     *
     * @return lista de médicos
     */
    public ArrayList<Medico> getMedicos() {
        return medicos;
    }

    /**
     * Busca un médico por su identificador único.
     *
     * @param id identificador del médico
     * @return el médico encontrado, o null
     *         si no existe ninguno con ese id
     */
    public Medico buscarMedicoPorId(int id) {
        for (Medico m : medicos) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    // RECETAS

    /**
     * Devuelve la lista completa de recetas registradas.
     *
     * @return lista de recetas
     */
    public ArrayList<Receta> getRecetas() {
        return recetas;
    }

    /**
     * Busca una receta por su identificador único.
     *
     * @param id identificador de la receta
     * @return la receta encontrada, o null
     *         si no existe ninguna con ese id
     */
    public Receta buscarRecetaPorId(int id) {
        for (Receta r : recetas) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    // ALERTAS

    /**
     * Añade una alerta al sistema asignándole un
     * identificador único.
     *
     * @param a alerta a registrar; su id
     *          se sobreescribe con el próximo disponible
     */
    public void agregarAlerta(Alerta a) {
        a.setId(nextIdAlerta);
        nextIdAlerta++;
        alertas.add(a);
    }

    /**
     * Devuelve la lista completa de alertas, incluyendo
     * las ya resueltas.
     *
     * @return lista de todas las alertas
     */
    public ArrayList<Alerta> getAlertas() {
        return alertas;
    }

    /**
     * Devuelve únicamente las alertas que no han sido
     * resueltas.
     *
     * @return lista de alertas activas
     */
    public ArrayList<Alerta> getAlertasActivas() {
        ArrayList<Alerta> activas = new ArrayList<>();
        for (Alerta a : alertas) {
            if (!a.isResuelta()) {
                activas.add(a);
            }
        }
        return activas;
    }

    /**
     * Comprueba si ya existe una alerta activa del tipo
     * dado para el medicamento indicado, para evitar
     * alertas duplicadas.
     *
     * @param idMedicamento identificador del medicamento
     * @param tipo          tipo de alerta a comprobar
     * @return true si existe una alerta activa
     *         de ese tipo para ese medicamento;
     *         false en caso contrario
     */
    public boolean existeAlertaActiva(int idMedicamento,
                                      TipoAlerta tipo) {
        for (Alerta a : alertas) {
            if (!a.isResuelta()
                    && a.getMedicamento().getId() == idMedicamento
                    && a.getTipo() == tipo) {
                return true;
            }
        }
        return false;
    }

    // ÓRDENES DE REPOSICIÓN

    /**
     * Añade una orden de reposición al sistema asignándole
     * un identificador único.
     *
     * @param o orden a registrar; su id
     *          se sobreescribe con el próximo disponible
     */
    public void agregarOrden(OrdenReposicion o) {
        o.setId(nextIdOrden);
        nextIdOrden++;
        ordenes.add(o);
    }

    /**
     * Devuelve la lista completa de órdenes de reposición.
     *
     * @return lista de órdenes
     */
    public ArrayList<OrdenReposicion> getOrdenes() {
        return ordenes;
    }

    /**
     * Busca una orden de reposición por su identificador
     * único.
     *
     * @param id identificador de la orden
     * @return la orden encontrada, o null
     *         si no existe ninguna con ese id
     */
    public OrdenReposicion buscarOrdenPorId(int id) {
        for (OrdenReposicion o : ordenes) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    /**
     * Revisa todos los medicamentos del inventario y
     * genera alertas de caducidad o stock mínimo cuando
     * es necesario.
     * 
     * Se genera una alerta de CADUCIDAD
     * si la fecha de caducidad del medicamento es anterior
     * a 30 días desde hoy, y una alerta de
     * STOCK_MINIMO si el stock actual
     * es igual o inferior al mínimo configurado. No se
     * crean alertas duplicadas para el mismo medicamento
     * y tipo. Este método debe llamarse al arrancar el
     * programa.
     */
    public void generarAlertasIniciales() {
        LocalDate hoy = LocalDate.now();
        LocalDate limiteCaducidad = hoy.plusDays(30);

        for (Medicamento m : medicamentos) {
            // Alerta si caduca en menos de 30 días
            if (m.getFechaCaducidad().isBefore(limiteCaducidad)) {
                if (!existeAlertaActiva(m.getId(), TipoAlerta.CADUCIDAD)) {
                    Alerta alerta = new Alerta(0, TipoAlerta.CADUCIDAD, m, hoy);
                    agregarAlerta(alerta);
                }
            }
            // Alerta si el stock está por debajo del mínimo
            if (m.getStock() <= m.getStockMinimo()) {
                if (!existeAlertaActiva(m.getId(), TipoAlerta.STOCK_MINIMO)) {
                    Alerta alerta = new Alerta(0, TipoAlerta.STOCK_MINIMO, m, hoy);
                    agregarAlerta(alerta);
                }
            }
        }
    }

    // GUARDAR Y CARGAR

    /**
     * Serializa el estado completo del sistema al fichero
     * data/farmacia.dat.
     * 
     * Crea la carpeta data/ si no existe. Los
     * errores de escritura se notifican por la salida
     * estándar sin interrumpir la ejecución.
     */
    public void guardar() {
        try {
            // Crear la carpeta data si no existe todavía
            File carpeta = new File("data");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            FileOutputStream fos = new FileOutputStream("data/farmacia.dat");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this);
            oos.close();
            fos.close();
        } catch (IOException e) {
            System.out.println("Error al guardar los datos: " + e.getMessage());
        }
    }

    /**
     * Carga el sistema siguiendo esta estrategia:
     * Si existe data/farmacia.dat, lo deserializa y lo devuelve.
     * Si existe data/datosIniciales.dat, lo deserializa como punto de partida.
     * Si ninguno existe, carga los datos de ejemplo "harcodeados", que se serializarán en
     * data/farmacia.dat una vez finalice el programa, y los devuelve.
     *
     * @return la instancia de SistemaFarmacia lista para usar
     */
    public static SistemaFarmacia cargar() {
        File fichero = new File("data/farmacia.dat");
        File inicial = new File("data/datosIniciales.dat");

        if (fichero.exists()) {
            try {
                FileInputStream fis = new FileInputStream(fichero);
                ObjectInputStream ois = new ObjectInputStream(fis);
                SistemaFarmacia sistema = (SistemaFarmacia) ois.readObject();
                ois.close();
                fis.close();
                return sistema;
            } catch (Exception e) {
                System.out.println("Error al cargar los datos guardados: " + e.getMessage());
                System.out.println("Intentando cargar datos iniciales...");
            }
        }

        if (inicial.exists()) {
            try {
                System.out.println("Primera ejecucion: cargando datos iniciales...");
                FileInputStream fis = new FileInputStream(inicial);
                ObjectInputStream ois = new ObjectInputStream(fis);
                SistemaFarmacia sistema = (SistemaFarmacia) ois.readObject();
                ois.close();
                fis.close();
                return sistema;
            } catch (Exception e) {
                System.out.println("Error al cargar datos iniciales: " + e.getMessage());
            }
        }

        System.out.println("Primera ejecucion: generando datos de ejemplo...");
        SistemaFarmacia s = new SistemaFarmacia();
        s.cargarDatosEjemplo();
        return s;
    }
    
    /**
     * Coordina la carga completa de datos de ejemplo en la primera ejecución.
     * Solo para uso interno en la carga de datos de ejemplo. 
     * 
     * Se ha usado para generar datosIniciales.dat, pero se ha mantenido también en código
     * para el caso de que se borren accidentalmente los archivos .dat
     */
    private void cargarDatosEjemplo() {
        cargarMedicamentosIniciales();
        cargarPacientesIniciales();
        cargarMedicosIniciales();
        cargarRecetasIniciales();
    }

    /**
     * Carga los 18 medicamentos de muestra, sus interacciones
     * peligrosas y sus alternativas.
     * Solo para uso interno en la carga de datos de ejemplo.
     */
    private void cargarMedicamentosIniciales() {
        //                                                                                             dosisMax  uds/caja
        medicamentos.add(new Medicamento( 1, "Paracetamol 500mg",    CategoriaMedicamento.ANALGESICO,        200, 30, LocalDate.of(2027,  6, 30),  0.15, false,  1000, 20));
        medicamentos.add(new Medicamento( 2, "Ibuprofeno 600mg",     CategoriaMedicamento.ANTIINFLAMATORIO,  150, 25, LocalDate.of(2027,  3, 15),  0.25, false,  2400, 20));
        medicamentos.add(new Medicamento( 3, "Amoxicilina 500mg",    CategoriaMedicamento.ANTIBIOTICO,        80, 15, LocalDate.of(2026,  8, 20),  0.45, false,  3000, 21));
        medicamentos.add(new Medicamento( 4, "Omeprazol 20mg",       CategoriaMedicamento.GASTROINTESTINAL,  120, 20, LocalDate.of(2027, 12,  1),  0.30, false,    40, 28));
        medicamentos.add(new Medicamento( 5, "Enalapril 10mg",       CategoriaMedicamento.ANTIHIPERTENSIVO,  100, 20, LocalDate.of(2027,  9, 10),  0.20, false,    40, 30));
        medicamentos.add(new Medicamento( 6, "Metformina 850mg",     CategoriaMedicamento.ANTIDIABETICO,      90, 15, LocalDate.of(2027,  5, 25),  0.35, false,  2550, 30));
        medicamentos.add(new Medicamento( 7, "Atorvastatina 20mg",   CategoriaMedicamento.CARDIOVASCULAR,     75, 10, LocalDate.of(2027, 11, 30),  0.55, false,    80, 30));
        medicamentos.add(new Medicamento( 8, "Diazepam 5mg",         CategoriaMedicamento.PSICOFARMACOS,      40,  8, LocalDate.of(2026,  7,  1),  0.80,  true,    30, 30));
        medicamentos.add(new Medicamento( 9, "Morfina 10mg",         CategoriaMedicamento.ANALGESICO,         20,  5, LocalDate.of(2026,  6, 15),  3.50,  true,    30, 10));
        medicamentos.add(new Medicamento(10, "Warfarina 5mg",        CategoriaMedicamento.ANTICOAGULANTE,     60, 10, LocalDate.of(2027,  4, 20),  0.90,  true,    15, 30));
        medicamentos.add(new Medicamento(11, "Salbutamol Inhalador", CategoriaMedicamento.RESPIRATORIO,       50, 10, LocalDate.of(2026, 12, 31),  4.20, false,   800, 200));
        medicamentos.add(new Medicamento(12, "Loratadina 10mg",      CategoriaMedicamento.OTRO,              130, 20, LocalDate.of(2027,  8, 15),  0.18, false,    20, 10));
        medicamentos.add(new Medicamento(13, "Clopidogrel 75mg",     CategoriaMedicamento.CARDIOVASCULAR,     65, 12, LocalDate.of(2027, 10,  5),  1.20, false,   300, 28));
        medicamentos.add(new Medicamento(14, "Doxorrubicina 50mg",   CategoriaMedicamento.ONCOLOGICO,         10,  3, LocalDate.of(2026,  5, 10), 85.00,  true,    50,  1));
        medicamentos.add(new Medicamento(15, "Azitromicina 500mg",   CategoriaMedicamento.ANTIBIOTICO,         8, 10, LocalDate.of(2026,  9, 30),  1.10, false,  1500,  6));
        medicamentos.add(new Medicamento(16, "Levotiroxina 50mcg",   CategoriaMedicamento.OTRO,               70, 15, LocalDate.of(2027,  7, 22),  0.28, false,   200, 30));
        medicamentos.add(new Medicamento(17, "Furosemida 40mg",      CategoriaMedicamento.CARDIOVASCULAR,     55, 10, LocalDate.of(2027,  1, 15),  0.22, false,   160, 30));
        medicamentos.add(new Medicamento(18, "Tramadol 100mg",       CategoriaMedicamento.ANALGESICO,         35,  8, LocalDate.of(2026,  5, 20),  1.60,  true,   400, 20));
        nextIdMedicamento = 19;

        // Interacciones peligrosas
        // Warfarina (10) interacciona con Ibuprofeno (2), Aspirina/Clopidogrel (13), Azitromicina (15)
        buscarMedicamentoPorId(10).getInteraccionesPeligrosas().add(2);
        buscarMedicamentoPorId( 2).getInteraccionesPeligrosas().add(10);
        buscarMedicamentoPorId(10).getInteraccionesPeligrosas().add(13);
        buscarMedicamentoPorId(13).getInteraccionesPeligrosas().add(10);
        buscarMedicamentoPorId(10).getInteraccionesPeligrosas().add(15);
        buscarMedicamentoPorId(15).getInteraccionesPeligrosas().add(10);
        // Diazepam (8) interacciona con Morfina (9) y Tramadol (18)
        buscarMedicamentoPorId( 8).getInteraccionesPeligrosas().add(9);
        buscarMedicamentoPorId( 9).getInteraccionesPeligrosas().add(8);
        buscarMedicamentoPorId( 8).getInteraccionesPeligrosas().add(18);
        buscarMedicamentoPorId(18).getInteraccionesPeligrosas().add(8);
        // Morfina (9) interacciona con Tramadol (18)
        buscarMedicamentoPorId( 9).getInteraccionesPeligrosas().add(18);
        buscarMedicamentoPorId(18).getInteraccionesPeligrosas().add(9);

        // Alternativas
        // Paracetamol (1) e Ibuprofeno (2) son alternativas entre sí
        añadirAlternativa(1, 2);
        // Amoxicilina (3) y Azitromicina (15) son alternativas entre sí
        añadirAlternativa(3, 15);
        // Enalapril (5) y Furosemida (17) como alternativas antihipertensivas
        añadirAlternativa(5, 17);
        // Morfina (9) y Tramadol (18) como alternativas analgésicas
        añadirAlternativa(9, 18);
    }

    /**
     * Carga los 8 pacientes de muestra.
     * Solo para uso interno en la carga de datos de ejemplo.
     */
    private void cargarPacientesIniciales() {
        pacientes.add(new Paciente(1, "María",   "García López",      LocalDate.of(1978,  4, 12), "12345678A"));
        pacientes.add(new Paciente(2, "Juan",    "Martínez Ruiz",     LocalDate.of(1965,  9, 23), "23456789B"));
        pacientes.add(new Paciente(3, "Ana",     "Fernández Torres",  LocalDate.of(1990,  1,  5), "34567890C"));
        pacientes.add(new Paciente(4, "Carlos",  "Pérez Sánchez",     LocalDate.of(1952, 11, 30), "45678901D"));
        pacientes.add(new Paciente(5, "Lucía",   "Ramírez Díaz",      LocalDate.of(2001,  7, 18), "56789012E"));
        pacientes.add(new Paciente(6, "Miguel",  "López González",    LocalDate.of(1973,  3,  7), "67890123F"));
        pacientes.add(new Paciente(7, "Elena",   "Jiménez Castro",    LocalDate.of(1987, 12, 14), "78901234G"));
        pacientes.add(new Paciente(8, "Roberto", "Morales Vega",      LocalDate.of(1944,  6, 25), "89012345H"));
    }

    /**
     * Carga los 5 médicos de muestra.
     * Solo para uso interno en la carga de datos de ejemplo.
     */
    private void cargarMedicosIniciales() {
        medicos.add(new Medico(1, "Pedro",  "Álvarez Moreno",  "Medicina Interna", "28-12345"));
        medicos.add(new Medico(2, "Carmen", "Ruiz Blanco",     "Cardiología",      "28-23456"));
        medicos.add(new Medico(3, "Javier", "Herrera Campos",  "Oncología",        "28-34567"));
        medicos.add(new Medico(4, "Sofía",  "Navarro Prieto",  "Endocrinología",   "28-45678"));
        medicos.add(new Medico(5, "Andrés", "Castillo Reyes",  "Neumología",       "28-56789"));

    }

    /**
     * Carga las 12 recetas de muestra que cubren todos los
     * estados y casos de uso del sistema.
     * Solo para uso interno en la carga de datos de ejemplo.
     */
    private void cargarRecetasIniciales() {
        Paciente p1 = buscarPacientePorId(1);
        Paciente p2 = buscarPacientePorId(2);
        Paciente p3 = buscarPacientePorId(3);
        Paciente p4 = buscarPacientePorId(4);
        Paciente p5 = buscarPacientePorId(5);
        Paciente p6 = buscarPacientePorId(6);
        Paciente p7 = buscarPacientePorId(7);
        Medico m1 = buscarMedicoPorId(1);
        Medico m2 = buscarMedicoPorId(2);
        Medico m4 = buscarMedicoPorId(4);
        Medico m5 = buscarMedicoPorId(5);

        Medicamento paracetamol    = buscarMedicamentoPorId(1);
        Medicamento ibuprofeno     = buscarMedicamentoPorId(2);
        Medicamento amoxicilina    = buscarMedicamentoPorId(3);
        Medicamento omeprazol      = buscarMedicamentoPorId(4);
        Medicamento enalapril      = buscarMedicamentoPorId(5);
        Medicamento metformina     = buscarMedicamentoPorId(6);
        Medicamento atorvastatina  = buscarMedicamentoPorId(7);
        Medicamento diazepam       = buscarMedicamentoPorId(8);
        Medicamento warfarina      = buscarMedicamentoPorId(10);
        Medicamento salbutamol     = buscarMedicamentoPorId(11);
        Medicamento azitromicina   = buscarMedicamentoPorId(15);
        Medicamento levotiroxina   = buscarMedicamentoPorId(16);

        LocalDate hoy = LocalDate.now();

        // Receta 1 - pendiente normal
        ArrayList<LineaReceta> l1 = new ArrayList<>();
        l1.add(new LineaReceta(paracetamol, 500, 3, 7));
        recetas.add(new Receta(1, p1, m1, l1, hoy.minusDays(1), false));

        // Receta 2 - pendiente con medicamento restringido -> PENDIENTE_AUTORIZACION
        ArrayList<LineaReceta> l2 = new ArrayList<>();
        l2.add(new LineaReceta(diazepam, 5, 1, 30));
        Receta r2 = new Receta(2, p2, m1, l2, hoy.minusDays(3), false);
        r2.setEstado(EstadoReceta.PENDIENTE_AUTORIZACION);
        recetas.add(r2);

        // Receta 3 - interacción peligrosa (Warfarina + Ibuprofeno) -> PENDIENTE_MEDICO
        ArrayList<LineaReceta> l3 = new ArrayList<>();
        l3.add(new LineaReceta(warfarina,  5, 1, 30));
        l3.add(new LineaReceta(ibuprofeno, 600, 3, 7));
        Receta r3 = new Receta(3, p3, m2, l3, hoy.minusDays(2), false);
        r3.setEstado(EstadoReceta.PENDIENTE_MEDICO);
        recetas.add(r3);

        // Receta 4 - dosis excesiva (Paracetamol 1500mg > máx 1000mg) -> PENDIENTE_MEDICO
        ArrayList<LineaReceta> l4 = new ArrayList<>();
        l4.add(new LineaReceta(paracetamol, 1500, 3, 5));
        Receta r4 = new Receta(4, p4, m1, l4, hoy.minusDays(1), false);
        r4.setEstado(EstadoReceta.PENDIENTE_MEDICO);
        recetas.add(r4);

        // Receta 5 - stock reservado (lista para dispensar)
        ArrayList<LineaReceta> l5 = new ArrayList<>();
        l5.add(new LineaReceta(omeprazol, 20, 1, 28));
        Receta r5 = new Receta(5, p5, m1, l5, hoy.minusDays(5), false);
        r5.setEstado(EstadoReceta.STOCK_RESERVADO);
        recetas.add(r5);

        // Receta 6 - crónica dispensada con dos líneas (tratamiento antihipertensivo + estatina)
        // Enalapril:    1 toma/día, 30 uds/caja -> 1 caja dura 30 días, próxima en hoy+3 (dentro de la semana)
        // Atorvastatina: 1 toma/día, 30 uds/caja -> 1 caja dura 30 días, próxima en hoy+6 (dentro de la semana)
        ArrayList<LineaReceta> l6 = new ArrayList<>();
        LineaReceta l6m1 = new LineaReceta(enalapril, 10, 1, 365);
        l6m1.setDuracionCaja(30);
        l6m1.setCajasRestantes(12);  // 13 cajas totales - 1 ya dispensada
        l6m1.setFechaProximaReposicion(hoy.plusDays(3)); // próxima dentro de 3 días -> detectable esta semana
        l6.add(l6m1);
        LineaReceta l6m2 = new LineaReceta(atorvastatina, 20, 1, 365);
        l6m2.setDuracionCaja(30);
        l6m2.setCajasRestantes(12);
        l6m2.setFechaProximaReposicion(hoy.plusDays(6)); // próxima dentro de 6 días -> también detectable esta semana
        l6.add(l6m2);
        Receta r6 = new Receta(6, p2, m2, l6, hoy.minusDays(30), true);
        r6.setEstado(EstadoReceta.DISPENSADA);
        r6.setFechaDispensacion(hoy.minusDays(30));
        recetas.add(r6);

        // Receta 7 - crónica dispensada con reposición próxima pero sin stock suficiente
        // Azitromicina tiene stock bajo (8 uds), 6 uds/caja, 1 toma/día -> 1 caja dura 6 días
        ArrayList<LineaReceta> l7 = new ArrayList<>();
        LineaReceta l7m1 = new LineaReceta(azitromicina, 500, 1, 180);
        l7m1.setDuracionCaja(6);
        l7m1.setCajasRestantes(29);
        l7m1.setFechaProximaReposicion(hoy.plusDays(1));
        l7.add(l7m1);
        Receta r7 = new Receta(7, p6, m1, l7, hoy.minusDays(6), true);
        r7.setEstado(EstadoReceta.DISPENSADA);
        r7.setFechaDispensacion(hoy.minusDays(6));
        recetas.add(r7);

        // Receta 8 - crónica dispensada con reposición lejana (no se activa esta semana)
        ArrayList<LineaReceta> l8 = new ArrayList<>();
        LineaReceta l8m1 = new LineaReceta(metformina, 850, 2, 365);
        l8m1.setDuracionCaja(15);
        l8m1.setCajasRestantes(24);
        l8m1.setFechaProximaReposicion(hoy.plusDays(20));
        l8.add(l8m1);
        Receta r8 = new Receta(8, p4, m4, l8, hoy.minusDays(5), true);
        r8.setEstado(EstadoReceta.DISPENSADA);
        r8.setFechaDispensacion(hoy.minusDays(5));
        recetas.add(r8);

        // Receta 9 - tratamiento con Atorvastatina, pendiente de dispensar (crónica)
        ArrayList<LineaReceta> l9 = new ArrayList<>();
        l9.add(new LineaReceta(atorvastatina, 20, 1, 365));
        Receta r9 = new Receta(9, p7, m2, l9, hoy, true);
        recetas.add(r9);

        // Receta 10 - Salbutamol inhalador (200 dosis/inhalador), 4 tomas/día, 30 días -> 1 caja
        ArrayList<LineaReceta> l10 = new ArrayList<>();
        l10.add(new LineaReceta(salbutamol, 100, 4, 30));
        recetas.add(new Receta(10, p5, m5, l10, hoy, false));

        // Receta 11 - Levotiroxina crónica, pendiente
        ArrayList<LineaReceta> l11 = new ArrayList<>();
        l11.add(new LineaReceta(levotiroxina, 50, 1, 365));
        recetas.add(new Receta(11, p7, m4, l11, hoy.minusDays(1), true));

        // Receta 12 - dispensada simple (historial)
        ArrayList<LineaReceta> l12 = new ArrayList<>();
        l12.add(new LineaReceta(amoxicilina, 500, 3, 7));
        Receta r12 = new Receta(12, p3, m1, l12, hoy.minusDays(10), false);
        r12.setEstado(EstadoReceta.DISPENSADA);
        r12.setFechaDispensacion(hoy.minusDays(10));
        recetas.add(r12);
    }
}

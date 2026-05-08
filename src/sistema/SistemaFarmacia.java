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
     * estado STOCK_RESERVADO.
     *
     * @param idMedicamento identificador del medicamento
     * @return stock disponible para nuevas reservas
     */
    public int calcularStockDisponible(int idMedicamento) {
        Medicamento m = buscarMedicamentoPorId(idMedicamento);
        if (m == null) return 0;
        return m.getStock()
                - calcularStockReservado(idMedicamento);
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
     * Realiza la comprobacion semanal de recetas cronicas dispensadas.
     *
     * Siete dias antes de fechaProximaReposicion comprueba el stock
     * disponible y genera una orden de reposicion si no hay suficiente.
     * El dia anterior notifica al paciente, descuenta una caja del
     * inventario y avanza fechaProximaReposicion al siguiente ciclo.
     * Se asume una linea por receta cronica.
     *
     * @return numero de ciclos avanzados (notificaciones enviadas)
     */
    public int comprobarReposicionCronicos() {
        LocalDate hoy = LocalDate.now();
        int procesadas = 0;
        for (Receta r : recetas) {
            if (!r.isCronica() || r.getEstado() != EstadoReceta.DISPENSADA) continue;
            LineaReceta l = r.getLineas().get(0);
            LocalDate proxima = l.getFechaProximaReposicion();
            if (proxima == null) continue;
            // 7 dias antes: comprobar stock y generar orden si hace falta
            if (!proxima.isAfter(hoy.plusDays(7))) {
                int disponible = calcularStockDisponible(l.getMedicamento().getId());
                if (disponible < 1 + l.getMedicamento().getStockMinimo()) {
                    int falta = 1 + l.getMedicamento().getStockMinimo() - disponible;
                    ArrayList<LineaOrden> lineasOrden = new ArrayList<>();
                    lineasOrden.add(new LineaOrden(l.getMedicamento(), falta));
                    agregarOrden(new OrdenReposicion(0, hoy, lineasOrden));
                    System.out.println("  [CRONICA] Sin stock para proxima caja de "
                            + l.getMedicamento().getNombre() + " ("
                            + r.getPaciente().getNombreCompleto()
                            + "). Orden de reposicion generada.");
                }
            }
            // El dia anterior: notificar al paciente y avanzar fecha
            if (proxima.isEqual(hoy.plusDays(1))) {
                System.out.println("  [AVISO] Paciente notificado: "
                        + r.getPaciente().getNombreCompleto()
                        + " - proxima caja de "
                        + l.getMedicamento().getNombre() + " disponible manana.");
                Medicamento m = buscarMedicamentoPorId(l.getMedicamento().getId());
                if (m != null) m.setStock(m.getStock() - 1);
                l.setFechaProximaReposicion(proxima.plusDays(l.getFrecuenciaDias()));
                procesadas++;
            }
        }
        return procesadas;
    }

    // PACIENTES

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

        System.out.println("No se encontraron datos. Iniciando sistema vacio.");
        return new SistemaFarmacia();
    }
}

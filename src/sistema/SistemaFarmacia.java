package sistema;

import model.*;
import model.enums.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;

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
    private int nextIdPaciente = 1;
    private int nextIdMedico = 1;
    private int nextIdReceta = 1;
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

    // PACIENTES

    /**
     * Añade un paciente al sistema asignándole un
     * identificador único.
     *
     * @param p paciente a registrar; su id
     *          se sobreescribe con el próximo disponible
     */
    public void agregarPaciente(Paciente p) {
        p.setId(nextIdPaciente);
        nextIdPaciente++;
        pacientes.add(p);
    }

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

    /**
     * Elimina el paciente con el identificador dado.
     *
     * @param id identificador del paciente a eliminar
     * @return true si se ha eliminado;
     *         false si no se encontró
     */
    public boolean eliminarPaciente(int id) {
        Paciente p = buscarPacientePorId(id);
        if (p != null) {
            pacientes.remove(p);
            return true;
        }
        return false;
    }

    // MÉDICOS

    /**
     * Añade un médico al sistema asignándole un
     * identificador único.
     *
     * @param m médico a registrar; su id
     *          se sobreescribe con el próximo disponible
     */
    public void agregarMedico(Medico m) {
        m.setId(nextIdMedico);
        nextIdMedico++;
        medicos.add(m);
    }

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

    /**
     * Elimina el médico con el identificador dado.
     *
     * @param id identificador del médico a eliminar
     * @return true si se ha eliminado;
     *         false si no se encontró
     */
    public boolean eliminarMedico(int id) {
        Medico m = buscarMedicoPorId(id);
        if (m != null) {
            medicos.remove(m);
            return true;
        }
        return false;
    }

    // RECETAS

    /**
     * Añade una receta al sistema asignándole un
     * identificador único.
     *
     * @param r receta a registrar; su id
     *          se sobreescribe con el próximo disponible
     */
    public void agregarReceta(Receta r) {
        r.setId(nextIdReceta);
        nextIdReceta++;
        recetas.add(r);
    }

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
     * Si ninguno existe, genera los datos de ejemplo en memoria, que se serializarán en
     * data/farmacia.dat, y los devuelve.
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

    private void cargarDatosEjemplo() {
        cargarMedicamentosIniciales();
        cargarPacientesIniciales();
        cargarMedicosIniciales();
    }

    private void cargarMedicamentosIniciales() {
        medicamentos.add(new Medicamento( 1, "Paracetamol 500mg",    CategoriaMedicamento.ANALGESICO,        200, 30, LocalDate.of(2027,  6, 30),  0.15, false));
        medicamentos.add(new Medicamento( 2, "Ibuprofeno 600mg",     CategoriaMedicamento.ANTIINFLAMATORIO,  150, 25, LocalDate.of(2027,  3, 15),  0.25, false));
        medicamentos.add(new Medicamento( 3, "Amoxicilina 500mg",    CategoriaMedicamento.ANTIBIOTICO,        80, 15, LocalDate.of(2026,  8, 20),  0.45, false));
        medicamentos.add(new Medicamento( 4, "Omeprazol 20mg",       CategoriaMedicamento.GASTROINTESTINAL,  120, 20, LocalDate.of(2027, 12,  1),  0.30, false));
        medicamentos.add(new Medicamento( 5, "Enalapril 10mg",       CategoriaMedicamento.ANTIHIPERTENSIVO,  100, 20, LocalDate.of(2027,  9, 10),  0.20, false));
        medicamentos.add(new Medicamento( 6, "Metformina 850mg",     CategoriaMedicamento.ANTIDIABETICO,      90, 15, LocalDate.of(2027,  5, 25),  0.35, false));
        medicamentos.add(new Medicamento( 7, "Atorvastatina 20mg",   CategoriaMedicamento.CARDIOVASCULAR,     75, 10, LocalDate.of(2027, 11, 30),  0.55, false));
        medicamentos.add(new Medicamento( 8, "Diazepam 5mg",         CategoriaMedicamento.PSICOFARMACOS,      40,  8, LocalDate.of(2026,  7,  1),  0.80,  true));
        medicamentos.add(new Medicamento( 9, "Morfina 10mg",         CategoriaMedicamento.ANALGESICO,         20,  5, LocalDate.of(2026,  6, 15),  3.50,  true));
        medicamentos.add(new Medicamento(10, "Warfarina 5mg",        CategoriaMedicamento.ANTICOAGULANTE,     60, 10, LocalDate.of(2027,  4, 20),  0.90,  true));
        medicamentos.add(new Medicamento(11, "Salbutamol Inhalador", CategoriaMedicamento.RESPIRATORIO,       50, 10, LocalDate.of(2026, 12, 31),  4.20, false));
        medicamentos.add(new Medicamento(12, "Loratadina 10mg",      CategoriaMedicamento.OTRO,              130, 20, LocalDate.of(2027,  8, 15),  0.18, false));
        medicamentos.add(new Medicamento(13, "Clopidogrel 75mg",     CategoriaMedicamento.CARDIOVASCULAR,     65, 12, LocalDate.of(2027, 10,  5),  1.20, false));
        medicamentos.add(new Medicamento(14, "Doxorrubicina 50mg",   CategoriaMedicamento.ONCOLOGICO,         10,  3, LocalDate.of(2026,  5, 10), 85.00,  true));
        medicamentos.add(new Medicamento(15, "Azitromicina 500mg",   CategoriaMedicamento.ANTIBIOTICO,        45, 10, LocalDate.of(2026,  9, 30),  1.10, false));
        medicamentos.add(new Medicamento(16, "Levotiroxina 50mcg",   CategoriaMedicamento.OTRO,               70, 15, LocalDate.of(2027,  7, 22),  0.28, false));
        medicamentos.add(new Medicamento(17, "Furosemida 40mg",      CategoriaMedicamento.CARDIOVASCULAR,     55, 10, LocalDate.of(2027,  1, 15),  0.22, false));
        medicamentos.add(new Medicamento(18, "Tramadol 100mg",       CategoriaMedicamento.ANALGESICO,         35,  8, LocalDate.of(2026,  5, 20),  1.60,  true));
        nextIdMedicamento = 19;
    }

    private void cargarPacientesIniciales() {
        Paciente p1 = new Paciente(1, "María",   "García López",      LocalDate.of(1978,  4, 12), "12345678A");
        p1.getAlergias().add("Penicilina");
        p1.getEnfermedadesCronicas().add("Hipertensión");
        p1.getEnfermedadesCronicas().add("Diabetes tipo 2");
        pacientes.add(p1);

        Paciente p2 = new Paciente(2, "Juan",    "Martínez Ruiz",     LocalDate.of(1965,  9, 23), "23456789B");
        p2.getEnfermedadesCronicas().add("Insuficiencia cardíaca");
        pacientes.add(p2);

        Paciente p3 = new Paciente(3, "Ana",     "Fernández Torres",  LocalDate.of(1990,  1,  5), "34567890C");
        p3.getAlergias().add("Ibuprofeno");
        p3.getAlergias().add("Aspirina");
        pacientes.add(p3);

        Paciente p4 = new Paciente(4, "Carlos",  "Pérez Sánchez",     LocalDate.of(1952, 11, 30), "45678901D");
        p4.getEnfermedadesCronicas().add("Diabetes tipo 2");
        p4.getEnfermedadesCronicas().add("EPOC");
        pacientes.add(p4);

        Paciente p5 = new Paciente(5, "Lucía",   "Ramírez Díaz",      LocalDate.of(2001,  7, 18), "56789012E");
        pacientes.add(p5);

        Paciente p6 = new Paciente(6, "Miguel",  "López González",    LocalDate.of(1973,  3,  7), "67890123F");
        p6.getAlergias().add("Sulfonamidas");
        p6.getEnfermedadesCronicas().add("Asma");
        pacientes.add(p6);

        Paciente p7 = new Paciente(7, "Elena",   "Jiménez Castro",    LocalDate.of(1987, 12, 14), "78901234G");
        p7.getEnfermedadesCronicas().add("Hipotiroidismo");
        pacientes.add(p7);

        Paciente p8 = new Paciente(8, "Roberto", "Morales Vega",      LocalDate.of(1944,  6, 25), "89012345H");
        p8.getEnfermedadesCronicas().add("Fibrilación auricular");
        p8.getEnfermedadesCronicas().add("Hipertensión");
        pacientes.add(p8);

        nextIdPaciente = 9;
    }

    private void cargarMedicosIniciales() {
        medicos.add(new Medico(1, "Pedro",  "Álvarez Moreno",  "Medicina Interna", "28-12345"));
        medicos.add(new Medico(2, "Carmen", "Ruiz Blanco",     "Cardiología",      "28-23456"));
        medicos.add(new Medico(3, "Javier", "Herrera Campos",  "Oncología",        "28-34567"));
        medicos.add(new Medico(4, "Sofía",  "Navarro Prieto",  "Endocrinología",   "28-45678"));
        medicos.add(new Medico(5, "Andrés", "Castillo Reyes",  "Neumología",       "28-56789"));
        nextIdMedico = 6;
    }
}

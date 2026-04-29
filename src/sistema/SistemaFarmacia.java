package sistema;

import model.*;
import model.enums.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Núcleo de datos del sistema de farmacia hospitalaria.
 * 
 * Almacena las listas de todas las entidades del dominio
 * (medicamentos, pacientes, médicos, recetas, alertas y
 * órdenes de reposición) y expone los métodos CRUD
 * necesarios para gestionarlas. Gestiona también la
 * persistencia mediante serialización Java al fichero
 * data/farmacia.dat y la carga inicial de datos
 * de muestra desde ficheros CSV.
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

    // ==================== MEDICAMENTOS ====================

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
            medicamentos.remove(m);
            return true;
        }
        return false;
    }

    // ==================== PACIENTES ====================

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

    // ==================== MÉDICOS ====================

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

    // ==================== RECETAS ====================

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

    // ==================== ALERTAS ====================

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

    // ==================== ÓRDENES DE REPOSICIÓN ====================

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

    // ==================== GUARDAR Y CARGAR ====================

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
     * Carga el sistema desde el fichero serializado
     * data/farmacia.dat.
     * 
     * Si el fichero no existe (primera ejecución) o no
     * puede leerse, crea un sistema nuevo con los datos
     * de muestra de los CSV de la carpeta data/.
     *
     * @return la instancia de SistemaFarmacia
     *         restaurada o de ejemplo
     */
    public static SistemaFarmacia cargar() {
        File fichero = new File("data/farmacia.dat");

        if (!fichero.exists()) {
            // Primera vez que se ejecuta el programa
            System.out.println("Primera ejecucion: cargando datos de ejemplo...");
            SistemaFarmacia s = new SistemaFarmacia();
            s.cargarDatosEjemplo();
            return s;
        }

        try {
            FileInputStream fis = new FileInputStream("data/farmacia.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            SistemaFarmacia sistema = (SistemaFarmacia) ois.readObject();
            ois.close();
            fis.close();
            return sistema;
        } catch (Exception e) {
            System.out.println("Error al cargar los datos guardados: " + e.getMessage());
            System.out.println("Iniciando con datos de ejemplo...");
            SistemaFarmacia s = new SistemaFarmacia();
            s.cargarDatosEjemplo();
            return s;
        }
    }

    // Lee los CSV de la carpeta data/ para tener datos de muestra la primera vez
    private void cargarDatosEjemplo() {
        cargarMedicamentosCSV();
        cargarPacientesCSV();
        cargarMedicosCSV();
    }

    private void cargarMedicamentosCSV() {
        File csv = new File("data/medicamentos.csv");
        if (!csv.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(csv));
            br.readLine(); // saltar la primera línea (cabecera)
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split(";");
                if (partes.length < 8) continue;

                int id = Integer.parseInt(partes[0].trim());
                String nombre = partes[1].trim();
                CategoriaMedicamento categoria = CategoriaMedicamento.valueOf(partes[2].trim());
                int stock = Integer.parseInt(partes[3].trim());
                int stockMinimo = Integer.parseInt(partes[4].trim());
                LocalDate fechaCaducidad = LocalDate.parse(partes[5].trim());
                double precio = Double.parseDouble(partes[6].trim());
                boolean restringido = Boolean.parseBoolean(partes[7].trim());

                Medicamento m = new Medicamento(id, nombre, categoria, stock, stockMinimo,
                        fechaCaducidad, precio, restringido);
                medicamentos.add(m);

                if (id >= nextIdMedicamento) {
                    nextIdMedicamento = id + 1;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error leyendo medicamentos.csv: " + e.getMessage());
        }
    }

    private void cargarPacientesCSV() {
        File csv = new File("data/pacientes.csv");
        if (!csv.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(csv));
            br.readLine(); // cabecera
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] p = linea.split(";", -1);
                if (p.length < 7) continue;

                int id = Integer.parseInt(p[0].trim());
                Paciente pac = new Paciente(id, p[1].trim(), p[2].trim(),
                        LocalDate.parse(p[3].trim()), p[4].trim());

                if (!p[5].trim().isEmpty()) {
                    pac.setAlergias(new ArrayList<>(Arrays.asList(p[5].trim().split("\\|"))));
                }
                if (!p[6].trim().isEmpty()) {
                    pac.setEnfermedadesCronicas(new ArrayList<>(Arrays.asList(p[6].trim().split("\\|"))));
                }
                pacientes.add(pac);

                if (id >= nextIdPaciente) {
                    nextIdPaciente = id + 1;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error leyendo pacientes.csv: " + e.getMessage());
        }
    }

    private void cargarMedicosCSV() {
        File csv = new File("data/medicos.csv");
        if (!csv.exists()) return;

        try {
            BufferedReader br = new BufferedReader(new FileReader(csv));
            br.readLine(); // cabecera
            String linea;
            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] p = linea.split(";", -1);
                if (p.length < 5) continue;

                int id = Integer.parseInt(p[0].trim());
                Medico m = new Medico(id, p[1].trim(), p[2].trim(), p[3].trim(), p[4].trim());
                medicos.add(m);

                if (id >= nextIdMedico) {
                    nextIdMedico = id + 1;
                }
            }
            br.close();
        } catch (Exception e) {
            System.out.println("Error leyendo medicos.csv: " + e.getMessage());
        }
    }
}

package sistema;

import model.*;
import model.enums.*;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

// Esta clase guarda todos los datos del programa y tiene los métodos para manejarlos.
// Actúa como el "núcleo" del sistema de farmacia.
public class SistemaFarmacia implements Serializable {

    private static final long serialVersionUID = 1L;

    // Listas donde guardamos todos los datos
    private ArrayList<Medicamento> medicamentos;
    private ArrayList<Paciente> pacientes;
    private ArrayList<Medico> medicos;
    private ArrayList<Receta> recetas;
    private ArrayList<Alerta> alertas;
    private ArrayList<OrdenReposicion> ordenes;

    // Contadores para asignar IDs únicos a cada entidad
    private int nextIdMedicamento = 1;
    private int nextIdPaciente = 1;
    private int nextIdMedico = 1;
    private int nextIdReceta = 1;
    private int nextIdAlerta = 1;
    private int nextIdOrden = 1;

    public SistemaFarmacia() {
        medicamentos = new ArrayList<>();
        pacientes = new ArrayList<>();
        medicos = new ArrayList<>();
        recetas = new ArrayList<>();
        alertas = new ArrayList<>();
        ordenes = new ArrayList<>();
    }

    // ==================== MEDICAMENTOS ====================

    public void agregarMedicamento(Medicamento m) {
        m.setId(nextIdMedicamento);
        nextIdMedicamento++;
        medicamentos.add(m);
    }

    public ArrayList<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public Medicamento buscarMedicamentoPorId(int id) {
        for (Medicamento m : medicamentos) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null; // no encontrado
    }

    public boolean eliminarMedicamento(int id) {
        Medicamento m = buscarMedicamentoPorId(id);
        if (m != null) {
            medicamentos.remove(m);
            return true;
        }
        return false;
    }

    // ==================== PACIENTES ====================

    public void agregarPaciente(Paciente p) {
        p.setId(nextIdPaciente);
        nextIdPaciente++;
        pacientes.add(p);
    }

    public ArrayList<Paciente> getPacientes() {
        return pacientes;
    }

    public Paciente buscarPacientePorId(int id) {
        for (Paciente p : pacientes) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    public boolean eliminarPaciente(int id) {
        Paciente p = buscarPacientePorId(id);
        if (p != null) {
            pacientes.remove(p);
            return true;
        }
        return false;
    }

    // ==================== MÉDICOS ====================

    public void agregarMedico(Medico m) {
        m.setId(nextIdMedico);
        nextIdMedico++;
        medicos.add(m);
    }

    public ArrayList<Medico> getMedicos() {
        return medicos;
    }

    public Medico buscarMedicoPorId(int id) {
        for (Medico m : medicos) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public boolean eliminarMedico(int id) {
        Medico m = buscarMedicoPorId(id);
        if (m != null) {
            medicos.remove(m);
            return true;
        }
        return false;
    }

    // ==================== RECETAS ====================

    public void agregarReceta(Receta r) {
        r.setId(nextIdReceta);
        nextIdReceta++;
        recetas.add(r);
    }

    public ArrayList<Receta> getRecetas() {
        return recetas;
    }

    public Receta buscarRecetaPorId(int id) {
        for (Receta r : recetas) {
            if (r.getId() == id) {
                return r;
            }
        }
        return null;
    }

    // ==================== ALERTAS ====================

    public void agregarAlerta(Alerta a) {
        a.setId(nextIdAlerta);
        nextIdAlerta++;
        alertas.add(a);
    }

    public ArrayList<Alerta> getAlertas() {
        return alertas;
    }

    public ArrayList<Alerta> getAlertasActivas() {
        ArrayList<Alerta> activas = new ArrayList<>();
        for (Alerta a : alertas) {
            if (!a.isResuelta()) {
                activas.add(a);
            }
        }
        return activas;
    }

    // Comprueba si ya existe una alerta activa del mismo tipo para ese medicamento
    // para no crear alertas duplicadas
    public boolean existeAlertaActiva(int idMedicamento, TipoAlerta tipo) {
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

    public void agregarOrden(OrdenReposicion o) {
        o.setId(nextIdOrden);
        nextIdOrden++;
        ordenes.add(o);
    }

    public ArrayList<OrdenReposicion> getOrdenes() {
        return ordenes;
    }

    public OrdenReposicion buscarOrdenPorId(int id) {
        for (OrdenReposicion o : ordenes) {
            if (o.getId() == id) {
                return o;
            }
        }
        return null;
    }

    // Revisa todos los medicamentos y genera alertas de caducidad y stock mínimo
    // Se llama al arrancar el programa
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

    // Carga el sistema desde el fichero guardado, o crea uno nuevo con datos de ejemplo
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

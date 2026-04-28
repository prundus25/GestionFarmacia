import sistema.SistemaFarmacia;
import ui.MenuPrincipal;

/**
 * Punto de entrada de la aplicación Farmacia Hospitalaria.
 * <p>
 * Carga o inicializa el {@link SistemaFarmacia}, genera
 * las alertas iniciales, lanza el menú principal y
 * guarda el estado al salir.
 */
public class Main {

    /**
     * Método principal de la aplicación.
     * <p>
     * Ejecuta el ciclo completo: carga de datos →
     * generación de alertas → menú interactivo →
     * persistencia al cerrar.
     *
     * @param args argumentos de línea de comandos
     *             (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("Cargando datos...");
        SistemaFarmacia sistema = SistemaFarmacia.cargar();

        // Revisar si hay alertas de caducidad o stock al arrancar
        sistema.generarAlertasIniciales();

        System.out.println("Sistema listo.\n");

        MenuPrincipal menu = new MenuPrincipal(sistema);
        menu.iniciar();

        // Guardamos todo antes de cerrar
        System.out.println("\nGuardando datos...");
        sistema.guardar();
        System.out.println("¡Hasta pronto!");
    }
}


import sistema.SistemaFarmacia;
import ui.MenuPrincipal;

/**
 * Punto de entrada de la aplicación Farmacia Hospitalaria.
 * Carga o inicializa el SistemaFarmacia, genera
 * las alertas iniciales, lanza el menú principal y
 * guarda el estado al salir.
 */
public class Main {
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
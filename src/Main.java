import sistema.SistemaFarmacia;
import ui.MenuPrincipal;

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



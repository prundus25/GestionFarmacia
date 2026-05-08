package ui;

import sistema.SistemaFarmacia;

/**
 * Menú principal de la aplicación.
 * Muestra el menú raíz y delega en los submenús de
 * inventario, recetas y alertas según la opción
 * elegida por el usuario. También informa del número
 * de alertas activas en cada iteración del bucle
 * principal.
 */
public class MenuPrincipal {

    private SistemaFarmacia sistema;

    /**
     * Crea un nuevo MenuPrincipal asociado al
     * sistema de farmacia dado.
     *
     * @param sistema instancia central del sistema
     */
    public MenuPrincipal(SistemaFarmacia sistema) {
        this.sistema = sistema;
    }

    /**
     * Lanza el bucle interactivo del menú principal.
     * Continúa mostrando opciones hasta que el usuario
     * elige la opción 0 (Salir).
     */
    public void iniciar() {
        System.out.println("============================================");
        System.out.println(" FARMACIA HOSPITALARIA - SISTEMA DE GESTION");
        System.out.println("============================================");

        boolean salir = false;
        while (!salir) {
            int numAlertas = sistema.getAlertasActivas().size();

            System.out.println();
            System.out.println("========== MENU PRINCIPAL ==========");
            if (numAlertas > 0) {
                System.out.println("  *** " + numAlertas + " ALERTA(S) ACTIVA(S) ***");
            }
            System.out.println("  1. Inventario de Medicamentos");
            System.out.println("  2. Gestion de Recetas");
            System.out.println("  3. Alertas y Reposiciones");
            System.out.println("  0. Salir");
            System.out.println("====================================");
            System.out.print("  Opcion: ");

            int opcion = Consola.leerEntero(0, 3);

            switch (opcion) {
                case 1 -> new MenuInventario(sistema).mostrar();
                case 2 -> new MenuRecetas(sistema).mostrar();
                case 3 -> new MenuAlertas(sistema).mostrar();
                case 0 -> salir = true;
            }
        }
    }

}



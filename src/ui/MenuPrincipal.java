package ui;

import sistema.SistemaFarmacia;

import java.util.Scanner;

public class MenuPrincipal {

    private SistemaFarmacia sistema;
    private Scanner scanner;

    public MenuPrincipal(SistemaFarmacia sistema) {
        this.sistema = sistema;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("============================================");
        System.out.println("  FARMACIA HOSPITALARIA - SISTEMA DE GESTION");
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
            System.out.println("  3. Gestion de Pacientes");
            System.out.println("  4. Gestion de Medicos");
            System.out.println("  5. Alertas y Reposiciones");
            System.out.println("  0. Salir");
            System.out.println("====================================");
            System.out.print("  Opcion: ");

            int opcion = leerEntero(0, 5);

            switch (opcion) {
                case 1:
                    new MenuInventario(sistema).mostrar();
                    break;
                case 2:
                    new MenuRecetas(sistema).mostrar();
                    break;
                case 3:
                    new MenuPacientes(sistema).mostrar();
                    break;
                case 4:
                    new MenuMedicos(sistema).mostrar();
                    break;
                case 5:
                    new MenuAlertas(sistema).mostrar();
                    break;
                case 0:
                    salir = true;
                    break;
            }
        }
    }

    private int leerEntero(int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.print("  Opcion invalida, elige entre " + min + " y " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero: ");
            }
        }
    }
}



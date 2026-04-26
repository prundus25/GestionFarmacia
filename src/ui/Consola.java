package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

// Clase con métodos de utilidad para leer datos por consola y pausar la pantalla.
// Al ser estáticos se pueden llamar directamente: Consola.leerEntero(0, 5)
public class Consola {

    // Scanner compartido por todos los menús
    public static final Scanner scanner = new Scanner(System.in);

    // Lee un entero entre min y max (inclusive). Repite hasta que el valor sea válido.
    public static int leerEntero(int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= min && valor <= max) {
                    return valor;
                }
                System.out.print("  Valor entre " + min + " y " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero: ");
            }
        }
    }

    // Lee un entero >= 0. Repite hasta que el valor sea válido.
    public static int leerEnteroPositivo() {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.nextLine().trim());
                if (valor >= 0) {
                    return valor;
                }
                System.out.print("  Numero positivo: ");
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero: ");
            }
        }
    }

    // Lee un número decimal. Acepta tanto punto como coma como separador.
    public static double leerDecimal() {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero decimal: ");
            }
        }
    }

    // Lee una fecha en formato dd/MM/yyyy. Repite hasta que el formato sea correcto.
    public static LocalDate leerFecha() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        while (true) {
            try {
                return LocalDate.parse(scanner.nextLine().trim(), fmt);
            } catch (DateTimeParseException e) {
                System.out.print("  Formato incorrecto (dd/MM/yyyy): ");
            }
        }
    }

    // Muestra un mensaje y espera a que el usuario pulse Enter.
    public static void pausar() {
        System.out.print("\n  Pulsa Enter para continuar...");
        scanner.nextLine();
    }
}

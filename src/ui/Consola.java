package ui;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utilidades estáticas de entrada/salida por consola
 * compartidas por todos los menús de la aplicación.
 * <p>
 * Todos los métodos de lectura muestran un mensaje de
 * error y repiten la solicitud hasta recibir un valor
 * válido, evitando el problema de caracteres residuales
 * en el buffer al utilizar siempre
 * {@link Scanner#nextLine()} en lugar de
 * {@link Scanner#nextInt()} o similares.
 */
public class Consola {

    /**
     * {@code Scanner} compartido por todos los menús para
     * leer la entrada estándar.
     */
    public static final Scanner scanner = new Scanner(System.in);

    /**
     * Lee un número entero introducido por el usuario
     * cuyo valor esté en el rango {@code [min, max]}
     * (ambos inclusive).
     * <p>
     * Repite la solicitud hasta recibir un valor válido.
     *
     * @param min valor mínimo aceptado (inclusive)
     * @param max valor máximo aceptado (inclusive)
     * @return el entero leído, garantizadamente en
     *         {@code [min, max]}
     */
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

    /**
     * Lee un número entero no negativo ({@code >= 0})
     * introducido por el usuario.
     * <p>
     * Repite la solicitud hasta recibir un valor válido.
     *
     * @return el entero leído, garantizadamente
     *         {@code >= 0}
     */
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

    /**
     * Lee un número decimal introducido por el usuario.
     * <p>
     * Acepta tanto el punto como la coma como separador
     * decimal. Repite la solicitud hasta recibir un valor
     * válido.
     *
     * @return el valor {@code double} leído
     */
    public static double leerDecimal() {
        while (true) {
            try {
                return Double.parseDouble(
                        scanner.nextLine().trim().replace(",", "."));
            } catch (NumberFormatException e) {
                System.out.print("  Escribe un numero decimal: ");
            }
        }
    }

    /**
     * Lee una fecha en formato {@code dd/MM/yyyy}
     * introducida por el usuario.
     * <p>
     * Repite la solicitud hasta recibir una cadena con
     * el formato correcto.
     *
     * @return el {@link LocalDate} correspondiente a la
     *         fecha introducida
     */
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

    /**
     * Muestra el mensaje «Pulsa Enter para continuar...»
     * y espera a que el usuario pulse la tecla Enter.
     */
    public static void pausar() {
        System.out.print("\n  Pulsa Enter para continuar...");
        scanner.nextLine();
    }
}

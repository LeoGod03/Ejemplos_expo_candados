class Cuenta {
    private int saldo = 1000;

    // El candado NO se declara aparte, se usa el "Monitor" del objeto
    public synchronized void retirar(String nombreHilo) {
        System.out.println("[" + nombreHilo + "] Entrando a sección crítica...");
        
        try {
            int temp = saldo;
            Thread.sleep(1000); // Simulamos retraso
            saldo = temp - 100;
            System.out.println("[" + nombreHilo + "] Retiro exitoso. Saldo: " + saldo);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Al llegar a esta llave '}', Java libera el candado AUTOMÁTICAMENTE
    }

    public int getSaldo() { return saldo; }
}

public class EjemploBanco {
    public static void main(String[] args) {
        Cuenta miCuenta = new Cuenta();

        // Creamos dos hilos que usan el MISMO objeto cuenta
        Thread hiloA = new Thread(() -> miCuenta.retirar("Hilo_A"));
        Thread hiloB = new Thread(() -> miCuenta.retirar("Hilo_B"));

        hiloA.start();
        hiloB.start();
    }
}

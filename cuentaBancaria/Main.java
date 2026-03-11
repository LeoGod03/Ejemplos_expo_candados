public class Main {

    public static void main(String[] args) throws InterruptedException {

        // Creamos dos cuentas (recursos compartidos)
        Cuenta cuentaA = new Cuenta();
        Cuenta cuentaB = new Cuenta();

        /*
         * Esta tarea representa una transferencia de dinero
         * entre dos cuentas. Se ejecutará concurrentemente.
         */
        Runnable tarea = () -> {

            for (int i = 0; i < 10000; i++) {

                // Adquirimos los candados antes de modificar los datos
                cuentaA.getCandado().lock();
                cuentaB.getCandado().lock();

                try {

                    // SECCIÓN CRÍTICA
                    if (cuentaA.getSaldo() > 0) {
                        cuentaA.retirar(1);
                        cuentaB.depositar(1);
                    }

                } finally {

                    // Siempre liberar los candados
                    cuentaB.getCandado().unlock();
                    cuentaA.getCandado().unlock();
                }
            }
        };

        // Creamos dos hilos que ejecutan la misma tarea
        Thread hilo1 = new Thread(tarea);
        Thread hilo2 = new Thread(tarea);

        // Iniciamos los hilos
        hilo1.start();
        hilo2.start();

        // Esperamos a que ambos terminen
        hilo1.join();
        hilo2.join();

        // Mostramos resultados
        System.out.println("Saldo Final Cuenta A: " + cuentaA.getSaldo());
        System.out.println("Saldo Final Cuenta B: " + cuentaB.getSaldo());
        System.out.println("Total: " + (cuentaA.getSaldo() + cuentaB.getSaldo()));
    }
}
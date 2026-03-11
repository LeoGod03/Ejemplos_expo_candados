public class ClaseConcurrencia {

    public static void main(String[] args) throws InterruptedException {

        Cuenta cuentaA = new Cuenta();
        Cuenta cuentaB = new Cuenta();

        Runnable tarea = () -> {

            for (int i = 0; i < 10000; i++) {

                cuentaA.getCandado().lock();
                cuentaB.getCandado().lock();

                try {

                    if (cuentaA.getSaldo() > 0) {
                        cuentaA.retirar(1);
                        cuentaB.depositar(1);
                    }

                } finally {

                    cuentaB.getCandado().unlock();
                    cuentaA.getCandado().unlock();
                }
            }
        };

        Thread hilo1 = new Thread(tarea);
        Thread hilo2 = new Thread(tarea);

        hilo1.start();
        hilo2.start();

        hilo1.join();
        hilo2.join();

        System.out.println("Saldo Final A: " + cuentaA.getSaldo());
        System.out.println("Saldo Final B: " + cuentaB.getSaldo());
        System.out.println("Total: " + (cuentaA.getSaldo() + cuentaB.getSaldo()));
    }
}
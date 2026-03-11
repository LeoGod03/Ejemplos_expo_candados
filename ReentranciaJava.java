import java.util.concurrent.locks.ReentrantLock;

public class ReentranciaJava {
    private final ReentrantLock lock = new ReentrantLock();

    // Ejemplo con synchronized (Reentrante por naturaleza)
    public synchronized void metodoA() {
        System.out.println("Hilo en Metodo A - Llamando a Metodo B...");
        metodoB(); // Si no fuera reentrante, aqui habria un deadlock
        System.out.println("Saliendo de Metodo A");
    }

    public synchronized void metodoB() {
        System.out.println("Hilo en Metodo B - Ejecucion exitosa");
    }

    // Ejemplo con ReentrantLock explicito
    public void metodoExplicito() {
        lock.lock();
        try {
            System.out.println("Lock adquirido una vez");
            lock.lock(); // Se adquiere por segunda vez (reentrancia)
            try {
                System.out.println("Lock adquirido por segunda vez (Contador = 2)");
            } finally {
                lock.unlock();
            }
        } finally {
            lock.unlock(); // Solo aqui queda libre para otros hilos
        }
    }

    public static void main(String[] args) {
        ReentranciaJava ejemplo = new ReentranciaJava();
        ejemplo.metodoA();
        ejemplo.metodoExplicito();
    }
}

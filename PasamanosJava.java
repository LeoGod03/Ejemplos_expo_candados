import java.util.concurrent.locks.ReentrantLock;

/**
 * Estructura de Nodo donde cada elemento tiene su propio candado independiente.
 */
class Nodo {
    int valor;
    Nodo siguiente;
    // Cada nodo es responsable de su propia exclusión mutua
    final ReentrantLock cerrojo = new ReentrantLock();

    public Nodo(int valor) {
        this.valor = valor;
        this.siguiente = null;
    }
}

/**
 * Clase principal que demuestra la técnica de Hand-over-Hand Locking.
 */
public class PasamanosJava {

    public void recorrerYProcesar(Nodo cabeza) {
        if (cabeza == null) return;

        Nodo actual = cabeza;

        // 1. Bloqueamos el primer nodo para asegurar la entrada a la lista
        actual.cerrojo.lock(); 
        System.out.println("[HILO] Bloqueado inicial: " + actual.valor);

        try {
            while (actual.siguiente != null) {
                Nodo siguiente = actual.siguiente;

                // 2. TÉCNICA PASAMANOS: Bloqueamos el siguiente ANTES de soltar el actual
                siguiente.cerrojo.lock();
                System.out.println("[HILO] Bloqueado siguiente: " + siguiente.valor);

                // 3. LIBERACIÓN TRASLAPADA: Soltamos el nodo anterior. 
                // Ahora otros hilos pueden entrar al nodo que acabamos de dejar.
                actual.cerrojo.unlock();
                System.out.println("[HILO] Liberado anterior: " + actual.valor);

                // Avanzamos en la lista
                actual = siguiente;
            }
        } finally {
            // 4. Al terminar el ciclo, debemos liberar el último nodo que quedó bloqueado
            if (actual.cerrojo.isHeldByCurrentThread()) {
                actual.cerrojo.unlock();
                System.out.println("[HILO] Liberado último nodo: " + actual.valor);
            }
        }
    }

    public static void main(String[] args) {
        // Creación de una lista enlazada simple: 10 -> 20 -> 30
        Nodo n1 = new Nodo(10);
        Nodo n2 = new Nodo(20);
        Nodo n3 = new Nodo(30);
        n1.siguiente = n2;
        n2.siguiente = n3;

        PasamanosJava demo = new PasamanosJava();
        System.out.println("Iniciando recorrido con ReentrantLock (Hand-over-Hand)...");
        demo.recorrerYProcesar(n1);
    }
}

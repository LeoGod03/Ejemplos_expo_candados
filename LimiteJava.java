class Nodo {
    int id;
    Nodo siguiente;

    public Nodo(int id) { this.id = id; }
}

public class LimiteJava {
    public static void recorrer(Nodo actual) {
        if (actual == null) return;

        // En Java, el candado está atado al BLOQUE { }
        synchronized (actual) {
            System.out.println("[JAVA] Bloqueado Nodo " + actual.id);
            
            if (actual.siguiente != null) {
                // Para bloquear el siguiente, entramos a otro nivel
                synchronized (actual.siguiente) {
                    System.out.println("[JAVA] Bloqueado Nodo " + actual.siguiente.id);
                    // Aquí Nodo 1 SIGUE bloqueado. No hay forma de soltarlo
                    // sin salir de la llave de abajo.
                } 
                System.out.println("[JAVA] Nodo " + actual.siguiente.id + " se liberó aquí.");
            }
            // AQUÍ es donde recién se libera el Nodo 1
        }
        System.out.println("[JAVA] Nodo " + actual.id + " se liberó aquí.");
    }

    public static void main(String[] args) {
        Nodo n1 = new Nodo(1);
        n1.siguiente = new Nodo(2);

        System.out.println("Intentando simular pasamanos en Java...");
        recorrer(n1);
    }
}

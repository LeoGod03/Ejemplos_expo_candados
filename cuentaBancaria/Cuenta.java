import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Cuenta {

    // Recurso compartido
    private int saldo = 10000;

    // Candado que protege el acceso al saldo
    private final Lock candado = new ReentrantLock();

    public void retirar(int cantidad) {
        saldo -= cantidad;
    }

    public void depositar(int cantidad) {
        saldo += cantidad;
    }

    public int getSaldo() {
        return saldo;
    }

    public Lock getCandado() {
        return candado;
    }
}
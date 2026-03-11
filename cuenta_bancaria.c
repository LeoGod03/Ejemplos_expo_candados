#include <stdio.h>
#include <pthread.h>
#include <unistd.h>

// El recurso compartido
int saldo_cuenta = 1000;

// El candado es una entidad INDEPENDIENTE
pthread_mutex_t mi_cerrojo;

void* realizar_transferencia(void* arg) {
    char* nombre_hilo = (char*)arg;

    printf("[%s] Intentando entrar al banco...\n", nombre_hilo);
    
    // BLOQUEO MANUAL
    pthread_mutex_lock(&mi_cerrojo); 
    
    printf("[%s] SECCIÓN CRÍTICA: Modificando saldo...\n", nombre_hilo);
    int temp = saldo_cuenta;
    sleep(1); // Simulamos un proceso lento para forzar la competencia
    saldo_cuenta = temp - 100;
    
    printf("[%s] Finalizado. Nuevo saldo: %d\n", nombre_hilo, saldo_cuenta);

    // DESBLOQUEO MANUAL (Si lo olvidas, el siguiente hilo nunca entra)
    pthread_mutex_unlock(&mi_cerrojo); 

    return NULL;
}

int main() {
    pthread_t hilo1, hilo2;

    // Inicialización del candado
    pthread_mutex_init(&mi_cerrojo, NULL);

    pthread_create(&hilo1, NULL, realizar_transferencia, "Hilo_A");
    pthread_create(&hilo2, NULL, realizar_transferencia, "Hilo_B");

    pthread_join(hilo1, NULL);
    pthread_join(hilo2, NULL);

    // Destrucción manual del candado
    pthread_mutex_destroy(&mi_cerrojo);

    printf("Saldo final en el banco: %d\n", saldo_cuenta);
    return 0;
}

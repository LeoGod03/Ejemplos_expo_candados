#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/mman.h>
#include <sys/wait.h>

int main() {
    // 1. Crear memoria compartida para el candado
    pthread_mutex_t *candado_compartido = mmap(NULL, sizeof(pthread_mutex_t), 
                                               PROT_READ | PROT_WRITE, 
                                               MAP_SHARED | MAP_ANONYMOUS, -1, 0);

    // 2. Configurar el candado para que sea "Process Shared"
    pthread_mutexattr_t attr;
    pthread_mutexattr_init(&attr);
    pthread_mutexattr_setpshared(&attr, PTHREAD_PROCESS_SHARED);

    // 3. Inicializar el candado en la memoria compartida con esos atributos
    pthread_mutex_init(candado_compartido, &attr);

    printf("--- Iniciando prueba de Fork CON memoria compartida ---\n");

    if (fork() == 0) { // Proceso HIJO
        pthread_mutex_lock(candado_compartido);
        printf("[HIJO] Bloqueé el candado COMPARTIDO. Entrando 3s...\n");
        sleep(3);
        printf("[HIJO] Liberando candado...\n");
        pthread_mutex_unlock(candado_compartido);
        exit(0);
    } else { // Proceso PADRE
        sleep(1);
        printf("[PADRE] Intentando bloquear el candado compartido...\n");
        
        // Aquí el PADRE SÍ SE QUEDARÁ ESPERANDO al hijo
        pthread_mutex_lock(candado_compartido);
        printf("[PADRE] ¡LOGRADO! Solo entré cuando el hijo terminó.\n");
        pthread_mutex_unlock(candado_compartido);

        wait(NULL);
    }

    // Limpieza
    pthread_mutex_destroy(candado_compartido);
    pthread_mutexattr_destroy(&attr);
    munmap(candado_compartido, sizeof(pthread_mutex_t));

    return 0;
}

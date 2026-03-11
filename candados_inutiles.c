#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <sys/wait.h>

// Variable global: se CLONARÁ en el fork
pthread_mutex_t candado_inutil;

int main() {
    pthread_mutex_init(&candado_inutil, NULL);

    printf("--- Iniciando prueba de Fork SIN memoria compartida ---\n");

    pid_t pid = fork();

    if (pid == 0) { // Proceso HIJO
        pthread_mutex_lock(&candado_inutil);
        printf("[HIJO] He bloqueado MI copia del candado. Entrando 3s...\n");
        sleep(3);
        printf("[HIJO] Saliendo y desbloqueando...\n");
        pthread_mutex_unlock(&candado_inutil);
        exit(0);
    } else { // Proceso PADRE
        sleep(1); // Esperamos a que el hijo bloquee primero
        printf("[PADRE] Intentando bloquear MI copia del candado...\n");
        
        // El padre NO se bloqueará aquí, porque su candado está libre
        pthread_mutex_lock(&candado_inutil);
        printf("[PADRE] ¡LOGRADO! Entré a la vez que el hijo porque los candados son copias distintas.\n");
        pthread_mutex_unlock(&candado_inutil);

        wait(NULL);
    }

    pthread_mutex_destroy(&candado_inutil);
    return 0;
}

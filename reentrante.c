#include <stdio.h>
#include <pthread.h>

// El mutex que será configurado como reentrante
pthread_mutex_t cerrojo_reentrante;

void funcion_interna() {
    printf("   [Interna] Intentando adquirir el mismo candado...\n");
    
    // Si el mutex no fuera recursivo, aquí el programa se detendría para siempre
    pthread_mutex_lock(&cerrojo_reentrante);
    printf("   [Interna] ¡Éxito! El hilo no se bloqueó a sí mismo.\n");
    
    pthread_mutex_unlock(&cerrojo_reentrante);
    printf("   [Interna] Candado liberado una vez.\n");
}

void funcion_principal() {
    printf("[Principal] Adquiriendo candado por primera vez...\n");
    pthread_mutex_lock(&cerrojo_reentrante);
    
    funcion_interna();
    
    printf("[Principal] De regreso en la función principal. Liberando...\n");
    pthread_mutex_unlock(&cerrojo_reentrante);
}

int main() {
    pthread_mutexattr_t atributos;

    // 1. Inicializar la estructura de atributos
    pthread_mutexattr_init(&atributos);

    // 2. Configurar el atributo para que el mutex sea RECURSIVO (Reentrante)
    // Esto permite que el mismo hilo lo bloquee múltiples veces
    pthread_mutexattr_settype(&atributos, PTHREAD_MUTEX_RECURSIVE);

    // 3. Inicializar el mutex usando los atributos configurados
    pthread_mutex_init(&cerrojo_reentrante, &atributos);

    printf("--- Demostración de Reentrancia en C ---\n");
    funcion_principal();

    // Limpieza
    pthread_mutex_destroy(&cerrojo_reentrante);
    pthread_mutexattr_destroy(&atributos);

    printf("Programa finalizado correctamente.\n");
    return 0;
}

#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

typedef struct Nodo {
    int id;
    pthread_mutex_t cerrojo;
    struct Nodo* siguiente;
} Nodo;

// Función para crear nodos
Nodo* crear_nodo(int id) {
    Nodo* nuevo = (Nodo*)malloc(sizeof(Nodo));
    nuevo->id = id;
    nuevo->siguiente = NULL;
    pthread_mutex_init(&nuevo->cerrojo, NULL);
    return nuevo;
}

void recorrer_lista_pasamanos(Nodo* cabeza) {
    Nodo *actual = cabeza;
    
    // 1. Bloqueamos el primero
    pthread_mutex_lock(&actual->cerrojo);
    printf("[HILO] Bloqueado Nodo %d\n", actual->id);

    while (actual->siguiente != NULL) {
        Nodo* siguiente = actual->siguiente;

        // 2. Bloqueamos el siguiente ANTES de soltar el actual
        pthread_mutex_lock(&siguiente->cerrojo);
        printf("[HILO] Bloqueado Nodo %d (Siguiente)\n", siguiente->id);

        // 3. SOLTAMOS el actual (Libertad total en C)
        pthread_mutex_unlock(&actual->cerrojo);
        printf("[HILO] Liberado Nodo %d (Anterior)\n", actual->id);

        actual = siguiente;
        sleep(1); // Pausa para que se vea el proceso en la consola
    }

    // 4. Soltamos el último
    pthread_mutex_unlock(&actual->cerrojo);
    printf("[HILO] Liberado Nodo %d (Final)\n", actual->id);
}

int main() {
    // Creamos una lista: 1 -> 2 -> 3
    Nodo* n1 = crear_nodo(1);
    Nodo* n2 = crear_nodo(2);
    Nodo* n3 = crear_nodo(3);
    n1->siguiente = n2;
    n2->siguiente = n3;

    printf("Iniciando recorrido Hand-over-Hand en C...\n");
    recorrer_lista_pasamanos(n1);

    return 0;
}
